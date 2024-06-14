# Establecer la imagen base
FROM node:14

# Establecer el directorio de trabajo en el contenedor
WORKDIR /equipo4-chatbot/Frontend

# Copiar los archivos del paquete.json y paquete-lock.json
COPY package*.json ./

# Instalar las dependencias del proyecto
RUN npm install

# Copiar el resto de los archivos de la aplicación
COPY . .

# Exponer el puerto que tu aplicación utilizará
EXPOSE 3000

# Comando para iniciar la aplicación
CMD [ "npm", "start" ]
