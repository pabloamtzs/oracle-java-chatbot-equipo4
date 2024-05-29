#!/bin/bash

SCRIPT_DIR=$(pwd)

# Verificar y obtener los valores necesarios
if [ -z "$DOCKER_REGISTRY" ]; then
    echo "DOCKER_REGISTRY not set. Will get it with state_get"
    export DOCKER_REGISTRY=$(state_get DOCKER_REGISTRY)
fi
if [ -z "$DOCKER_REGISTRY" ]; then
    echo "Error: DOCKER_REGISTRY env variable needs to be set!"
    exit 1
fi

# Definir el tiempo actual para nombrar el archivo de despliegue
export CURRENTTIME=$(date '+%F_%H:%M:%S')
echo "CURRENTTIME is $CURRENTTIME ... this will be appended to generated deployment yaml"

# Copiar el archivo de despliegue original y reemplazar las variables
cp src/main/resources/deployment.yaml equipo4-chatbot-$CURRENTTIME.yaml

# Detectar el sistema operativo y ajustar la sintaxis de sed
if [[ "$OSTYPE" == "darwin"* ]]; then
    echo "Running on macOS"
    sed -i '' "s|%DOCKER_REGISTRY%|${DOCKER_REGISTRY}|g" equipo4-chatbot-$CURRENTTIME.yaml
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    echo "Running on Linux"
    sed -i "s|%DOCKER_REGISTRY%|${DOCKER_REGISTRY}|g" equipo4-chatbot-$CURRENTTIME.yaml
else
    echo "Unsupported operating system"
    exit 1
fi

# Aplicar el archivo de despliegue a Kubernetes
if [ -z "$1" ]; then
    kubectl apply -f "$SCRIPT_DIR"/equipo4-chatbot-$CURRENTTIME.yaml -n equipo4-chatbot
else
    kubectl apply -f <(istioctl kube-inject -f "$SCRIPT_DIR"/equipo4-chatbot-$CURRENTTIME.yaml) -n equipo4-chatbot
fi
