#!/bin/bash
# Copyright (c) 2022 Oracle and/or its affiliates.
# Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.

# Make sure this is run via source or .
if ! (return 0 2>/dev/null); then
  echo "ERROR: Usage 'source setup.sh'"
  exit
fi

if state_done SETUP; then
  echo "The setup has been completed"
  return
fi

# Detectar el sistema operativo
if [[ "$OSTYPE" == "darwin"* ]]; then
    echo "Running on macOS"
    SETUP_SCRIPT="$EQUIPO4_CHATBOT_LOCATION/utils/main-setup.sh"
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    echo "Running on Linux"
    SETUP_SCRIPT="$EQUIPO4_CHATBOT_LOCATION/utils/main-setup.sh"
else
    echo "Unsupported operating system"
    exit 1
fi

# Verificar si el script de configuración ya está en ejecución
if ps -ef | grep "$SETUP_SCRIPT" | grep -v grep; then
  echo "The '$SETUP_SCRIPT' is already running. If you want to restart it then kill it and then rerun."
else
  # Ejecutar el script de configuración correspondiente al sistema operativo
  "$SETUP_SCRIPT" 2>&1 | tee -ai "$EQUIPO4_CHATBOT_LOG/main-setup.log"
fi
