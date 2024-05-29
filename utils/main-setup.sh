#!/bin/bash
# Copyright (c) 2021 Oracle and/or its affiliates.
# Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.

# Fail on error
set -e

#Check if home is set
if test -z "$EQUIPO4_CHATBOT_LOCATION"; then
  echo "ERROR: this script requires EQUIPO4_CHATBOT_LOCATION to be set"
  exit
fi

#Exit if we are already done
if state_done SETUP_VERIFIED; then
  echo "SETUP_VERIFIED completed"
  exit
fi

#Identify Run Type
while ! state_done RUN_TYPE; do
    state_set RUN_TYPE "1"
done


# Get the User OCID
while ! state_done USER_OCID; do
  if test -z "$TEST_USER_OCID"; then
    read -p "Please enter your OCI user's OCID: " USER_OCID
  else # this gets used in the terraform file
    USER_OCID="$TEST_USER_OCID"
  fi
  # Validate
  if test "$(oci iam user get --user-id "$USER_OCID" --query 'data."lifecycle-state"' --raw-output 2>"$EQUIPO4_CHATBOT_LOG/user_ocid_err")" == 'ACTIVE'; then
    state_set USER_OCID "$USER_OCID"
  else
    echo "That user OCID could not be validated"
    cat "$EQUIPO4_CHATBOT_LOG/user_ocid_err"
  fi
done

while ! state_done USER_NAME; do
  USER_NAME=$(oci iam user get --user-id "$(state_get USER_OCID)" --query "data.name" --raw-output)
  state_set USER_NAME "$USER_NAME"
done

# Get the tenancy OCID
while ! state_done TENANCY_OCID; do
  if test -z "$TEST_TENANCY_OCID"; then
    read -p "Please enter your OCI tenancy's OCID: " TENANCY_OCID
  else # this gets used in the terraform file
    TENANCY_OCID="$TEST_TENANCY_OCID"
  fi
  # Validate
  if test -n "$(oci iam tenancy get --tenancy-id "$TENANCY_OCID" 2>"$EQUIPO4_CHATBOT_LOG/tenancy_ocid_err")"; then
    state_set TENANCY_OCID "$TENANCY_OCID"
  else
    echo "That tenancy OCID could not be validated"
    cat "$EQUIPO4_CHATBOT_LOG/tenancy_ocid_err"
  fi
done

# Double check and then set the region
while ! state_done REGION; do
  if test "$(state_get RUN_TYPE)" -eq 1; then
    HOME_REGION=$(oci iam region-subscription list --query 'data[?"is-home-region"]."region-name" | join('\'' '\'', @)' --raw-output)
    state_set HOME_REGION "$HOME_REGION"
  fi
  state_set REGION "$HOME_REGION" # Set in cloud shell env
done

#create the compartment
##newest code added later
while ! state_done COMPARTMENT_OCID; do
  if test "$(state_get RUN_TYPE)" -ne 3; then
    read -p "if you have your own compartment, enter it here: (if not, hit enter) " COMPARTMENT_OCID
    ##newest condition added
    if test -n "$COMPARTMENT_OCID" && test "$(oci iam compartment get --compartment-id "$COMPARTMENT_OCID" --query 'data."lifecycle-state"' --raw-output 2>/dev/null)" == 'ACTIVE'; then
      state_set COMPARTMENT_OCID "$COMPARTMENT_OCID"
    else
      echo "Resources will be created in a new compartment named $(state_get RUN_NAME)"
      COMPARTMENT_OCID=$(oci iam compartment create --compartment-id "$(state_get TENANCY_OCID)" --name "$(state_get RUN_NAME)" --description "mtdrworkshop" --query 'data.id' --raw-output)
    fi
  fi
  while ! test "$(oci iam compartment get --compartment-id "$COMPARTMENT_OCID" --query 'data."lifecycle-state"' --raw-output 2>/dev/null)" == 'ACTIVE'; do
    echo "Waiting for the compartment to become ACTIVE"
    sleep 60
  done
  state_set COMPARTMENT_OCID "$COMPARTMENT_OCID"
done

# Get Namespace
while ! state_done NAMESPACE; do
  NAMESPACE=$(oci os ns get --compartment-id "$(state_get COMPARTMENT_OCID)" --query "data" --raw-output)
  state_set NAMESPACE "$NAMESPACE"
done

# Get Container Repository
while ! state_done CR_NAME; do
  state_set CR_NAME "equipo-4_container_repository"
done

# login to docker
while ! state_done DOCKER_REGISTRY; do
  if test $(state_get RUN_TYPE) -ne 3; then
    ##export OCI_CLI_PROFILE=$(state_get HOME_REGION) ## have to get rid of this for non instance_principal based stuff
    read -s -r -p "Please generate an Auth Token and enter the value: " TOKEN
    echo
    echo "Auth Token entry accepted.  Attempting docker login."
  fi

  RETRIES=0
  while test $RETRIES -le 30; do
    echo "Attempt: $RETRIES"
    if echo "$TOKEN" | docker login -u "$(state_get NAMESPACE)/$(state_get USER_NAME)" --password-stdin "$(state_get REGION).ocir.io" &>/dev/null; then
      echo "Docker login completed"
      state_set DOCKER_REGISTRY "$(state_get REGION).ocir.io/$(state_get NAMESPACE)/$(state_get CR_NAME)"
      export OCI_CLI_PROFILE=$(state_get REGION)
      break
    else
      # echo "Docker login failed.  Retrying"
      RETRIES=$((RETRIES+1))
      sleep 5
    fi
  done
done