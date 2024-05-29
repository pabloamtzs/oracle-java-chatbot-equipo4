#!/bin/bash
# Copyright (c) 2022 Oracle and/or its affiliates.
# Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.

# Make sure this is run via source or .
if ! (return 0 2>/dev/null); then
  echo "ERROR: Usage 'source env.sh'"
  exit
fi

# POSIX compliant find and replace
function sed_i(){
  local OP="$1"
  local FILE="$2"
  sed -e "$OP" "$FILE" >"/tmp/$FILE"
  mv -- "/tmp/$FILE" "$FILE"
}
export -f sed_i

#set equipo4_chatbot_location
export EQUIPO4_CHATBOT_LOCATION="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd "$EQUIPO4_CHATBOT_LOCATION"
echo "EQUIPO4_CHATBOT_LOCATION: $EQUIPO4_CHATBOT_LOCATION"

# Java Home
# -d true if file is a directory, so it's testing if this directory exists, if it does
# we are on Mac doing local dev
if test -d ~/graalvm-ce-java11-20.1.0/Contents/Home/bin; then
  # We are on Mac doing local dev
  export JAVA_HOME=~/graalvm-ce-java11-20.1.0/Contents/Home;
else
  # Assume linux
  export JAVA_HOME=~/graalvm-ce-java11-20.1.0
fi
export PATH="$JAVA_HOME/bin:$PATH"

#state directory
if test -d ~/equipo4-chatbot-state; then
  export EQUIPO4_CHATBOT_STATE_HOME=~/equipo4-chatbot-state
else
  export EQUIPO4_CHATBOT_STATE_HOME="$EQUIPO4_CHATBOT_LOCATION"
fi
echo "EQUIPO4_CHATBOT_STATE_HOME: $EQUIPO4_CHATBOT_STATE_HOME"

#Log Directory
export EQUIPO4_CHATBOT_LOG="$EQUIPO4_CHATBOT_STATE_HOME/log"
mkdir -p "$EQUIPO4_CHATBOT_LOG"

source "$EQUIPO4_CHATBOT_LOCATION/utils/state-functions.sh"

# SHORTCUT ALIASES AND UTILS...
alias k='kubectl'
alias kt='kubectl --insecure-skip-tls-verify'
alias pods='kubectl get po --all-namespaces'
alias services='kubectl get services --all-namespaces'
alias gateways='kubectl get gateways --all-namespaces'
alias secrets='kubectl get secrets --all-namespaces'
alias ingresssecret='kubectl get secrets --all-namespaces | grep istio-ingressgateway-certs'
alias virtualservices='kubectl get virtualservices --all-namespaces'
alias deployments='kubectl get deployments --all-namespaces'
alias equipo4-chatbot='echo deployments... ; deployments|grep equipo4-chatbot ; echo pods... ; pods|grep equipo4-chatbot ; echo services... ; services | grep equipo4-chatbot ; echo secrets... ; secrets|grep equipo4-chatbot ; echo "other shortcut commands... most can take partial podname as argument, such as [logpod front] or [deletepod order]...  pods  services secrets deployments " ; ls "$EQUIPO4_CHATBOT_LOCATION"/utils/'

export PATH="$PATH:$EQUIPO4_CHATBOT_LOCATION/utils/"