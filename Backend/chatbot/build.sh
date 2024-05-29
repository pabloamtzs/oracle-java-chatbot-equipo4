#!/bin/bash

# Habilitar BuildKit
export DOCKER_BUILDKIT=1

export IMAGE_NAME=equipo4-chatbot-springboot
export IMAGE_VERSION=0.1

if [ -z "$DOCKER_REGISTRY" ]; then
    echo "DOCKER_REGISTRY not set. Will get it with state_get"
  export DOCKER_REGISTRY=$(state_get DOCKER_REGISTRY)
fi

if [ -z "$DOCKER_REGISTRY" ]; then
    echo "Error: DOCKER_REGISTRY env variable needs to be set!"
    exit 1
fi

export IMAGE=${DOCKER_REGISTRY}/${IMAGE_NAME}:${IMAGE_VERSION}
echo "Using Docker registry: $DOCKER_REGISTRY"
echo "Full image name: $IMAGE"

# Obtener secretos desde Kubernetes y guardarlos como archivos temporales
kubectl get secret dbuser -o jsonpath='{.data.dbusername}'  --namespace=equipo4-chatbot | base64 --decode > db_user.secret
kubectl get secret dbuser -o jsonpath='{.data.dbpassword}'  --namespace=equipo4-chatbot | base64 --decode > db_password.secret

# Obtener secretos desde Kubernetes y guardarlos como archivos temporales
kubectl get secret db-wallet-secret --namespace=equipo4-chatbot -o json > secret.json

# Decodificar y guardar los archivos de la wallet
mkdir -p tns_admin
jq -r '.data | to_entries[] | .key + " " + .value' secret.json | while read -r key value; do
    echo "$value" | base64 --decode > "tns_admin/$key"
done
tar -czf wallet_secret.tar.gz -C tns_admin .


# Verificar si los archivos existen
if [ ! -f "db_user.secret" ]; then
    echo "Error: db_user.secret not found!"
    exit 1
fi

if [ ! -f "db_password.secret" ]; then
    echo "Error: db_password.secret not found!"
    exit 1
fi

if [ ! -d "tns_admin" ]; then
    echo "Error: tns_admin directory not found!"
    exit 1
fi

# Construir la imagen Docker usando secretos
DOCKER_BUILDKIT=1 docker build \
    --secret id=db_user,src=db_user.secret \
    --secret id=db_password,src=db_password.secret \
    --secret id=wallet_secret,src=wallet_secret.tar.gz \
    -t $IMAGE .

# Limpiar archivos temporales
rm db_user.secret db_password.secret secret.json
rm -r tns_admin
rm wallet_secret.tar.gz

# Pushear la imagen al registry
docker push ${IMAGE}
if [ $? -eq 0 ]; then
    echo "Docker image pushed successfully."
    docker rmi "${IMAGE}"
else
    echo "Error: Docker image push failed."
fi