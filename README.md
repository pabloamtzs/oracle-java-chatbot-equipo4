# Oracle Java Chatbot Equipo 4
Repositorio del proyecto Oracle Java Chatbot del Equipo 4 para la materia de Desarrollo e implantación de sistemas de software

# Actividad de Aprendizaje 2:
Siguiendo los lineamientos que hemos estado trabajando a lo largo del semestre con el proyecto del reto y las tecnologías que nos han dado a estudiar y trabajar “workshops”, llegué a los siguientes puntos de trabajo y componentes principales:

1. Requerimientos del proyecto:
- Backend:
Lenguaje de programación: Java
Framework de desarrollo: Spring Boot
Base de datos: Oracle Autonomous Database
- Frontend (para la interfaz de administración del mánager):
Telegram API para la integración con Telegram
Posiblemente un framework web para construir una interfaz web de administración para el mánager, como Angular o React.

2. Utilidad de los diversos frameworks de desarrollo:
Spring Boot: Es un framework de aplicación Java para crear aplicaciones independientes y basadas en producción de manera más rápida. Proporciona un entorno preconfigurado y opinionado, lo que facilita el desarrollo y la implementación.
Angular o React: Este framework de desarrollo frontend popular para la creación de interfaces de usuario interactivas y de una sola página. Proporciona un enfoque estructurado para el desarrollo frontend, facilitando la creación de interfaces de usuario complejas y dinámicas.

3. Bibliotecas o frameworks auxiliares necesarios:
Telegram API: Para integrar el ChatBot con la plataforma de Telegram.
Oracle JDBC Driver: Para conectarse a la base de datos Oracle desde la aplicación Java.
Spring Data JPA: Para la capa de persistencia y la interacción con la base de datos en el backend.
Spring Security: Para la autenticación y autorización de usuarios en la aplicación.

4. Selección y justificación de las herramientas tecnológicas:
- Backend:
•	Spring Boot: Es una opción sólida para el desarrollo backend en Java, proporcionando una estructura y configuración predefinidas que aceleran el desarrollo. Además, ofrece soporte para la integración con bases de datos Oracle y la implementación de API RESTful, que serán útiles para este proyecto.

•	Oracle Autonomous Database: Proporciona una base de datos gestionada y escalable que puede adaptarse fácilmente a las necesidades del proyecto, además de ofrecer características de seguridad y rendimiento.
- Frontend:
•	Telegram API: Es esencial para la integración con Telegram y permitirá la interacción del ChatBot con los usuarios a través de la plataforma.

•	Angular o React: La elección entre estos frameworks dependerá de las preferencias del equipo de desarrollo y de la complejidad de la interfaz de administración que se desee construir. Cada uno de estos frameworks tiene sus ventajas y desventajas, pero todos son adecuados para crear interfaces de usuario robustas y dinámicas.
En resumen, el uso de Spring Boot para el backend proporcionará una base sólida y eficiente para el desarrollo de la lógica de negocio y la interacción con la base de datos Oracle, mientras que la elección de Angular o React para el frontend dependerá de las necesidades específicas de la interfaz de administración y las preferencias del equipo de desarrollo. La integración con la API de Telegram será crucial para permitir la comunicación bidireccional entre el ChatBot y los usuarios a través de Telegram.

# Actividad de Aprendizaje 6

# Funcionalidades del Servicio Web

## Descripción General del Back-end

El back-end del proyecto estará compuesto por varios microservicios, cada uno responsable de una funcionalidad específica. Estos microservicios seguirán una arquitectura de servicios RESTful y estarán implementados dentro de un contenedor en Docker, desplegados por medio de un clúster de Kubernetes en la nube de Oracle. Por lo que se desarrollarán microservicios para la API, que actuarán como una interfaz para la comunicación entre el front-end y el back-end del sistema. En específico el back-end establecerá la conexión con la base de datos Autonomous Oracle Database para almacenar y recuperar datos necesarios para el funcionamiento del servicio Web.

## Endpoints, Métodos HTTP y Estructura del Body

### Conexión con la Base de Datos

- **Microservicio de API:**
  - **Descripción:** Este microservicio actuará como una API que proporciona una interfaz para la comunicación entre el front-end y el back-end del sistema. Manejará las solicitudes HTTP entrantes, las procesará y las enviará al microservicio correspondiente para su ejecución. Además, devolverá las respuestas apropiadas al front-end.
  - **Credenciales y Tokens:** Se utilizarán credenciales y tokens proporcionados por Oracle Cloud Infrastructure para acceder a la base de datos dentro de la red privada hospedada en OCI.
  - **Tecnología:** Spring Boot con Spring MVC.
  - **Endpoint:** `/api/*`
  - **Métodos HTTP:** GET, POST, PUT, DELETE, etc., según las necesidades de las funcionalidades.
  - **Estructura del Body:** Dependerá de la funcionalidad específica que se esté solicitando.
  - **Respuestas:** Las respuestas variarán según la acción realizada y la funcionalidad asociada.
    - **Conexión exitosa:** El back-end podrá realizar operaciones de lectura y escritura en la base de datos.
    - **Error de conexión:** Se manejarán errores de conexión adecuadamente para informar al usuario y al equipo de desarrollo.

### Servicios de login y autenticación

- **Autenticación de Usuarios:**
  - **Descripción:** Permite a los usuarios autenticarse en el sistema.
  - **Endpoint:** `/api/auth/login`
  - **Método HTTP:** POST
  - **Body:**
    ```json
    {
      "IdEmpleado": "string",
      "password": "string"
    }
    ```
  - **Validaciones:**
    - El campo `IdEmpleado` y `password` son obligatorios.
  - **Respuestas:**
    - `200 OK`: Autenticación exitosa.
    - `401 Unauthorized`: Credenciales inválidas.

- **Registro de Usuarios:**
  - **Descripción:** Permite a los usuarios registrarse en el sistema.
  - **Endpoint:** `/api/auth/register`
  - **Método HTTP:** POST
  - **Body:**
    ```json
    {
      "IdEmpleado": "string",
      "password": "string",
      "email": "string"
    }
    ```
  - **Validaciones:**
    - Los campos `IdEmpleado`, `password` y `email` son obligatorios.
  - **Respuestas:**
    - `201 Created`: Registro exitoso.
    - `400 Bad Request`: Error en los datos proporcionados.

### Servicios CRUD tareas

- **Crear Tarea:**
  - **Descripción:** Permite crear una nueva tarea en el sistema.
  - **Endpoint:** `/api/tareas`
  - **Método HTTP:** POST
  - **Body:**
    ```json
    {
      "descripcionTarea": "string",
      "estado": "string",
      "IdSprint": "string"
    }
    ```
  - **Validaciones:**
    - Los campos `descripcionTarea`, `estado` y `idSprint` son obligatorios.
  - **Respuestas:**
    - `201 Created`: Tarea creada exitosamente.
    - `400 Bad Request`: Error en los datos proporcionados.

- **Eliminar Tarea:**
  - **Descripción:** Permite eliminar una tarea existente en el sistema.
  - **Endpoint:** `/api/tareas/{IdTarea}`
  - **Método HTTP:** DELETE
  - **Respuestas:**
    - `204 No Content`: Tarea eliminada exitosamente.
    - `404 Not Found`: Tarea no encontrada.

- **Obtener Tareas de Todos los Miembros del Equipo:**
  - **Descripción:** Permite a los líderes de proyecto obtener todas las tareas asignadas a los miembros de su equipo.
  - **Endpoint:** `/api/tareas/all`
  - **Método HTTP:** GET
  - **Body:**
    ```json
    {
      "IdEmpleado": "string"
    }
    ```
  - **Validación:**
    - El campo `IdEmpleado` es obligatorio.
  - **Respuestas:**
    - `200 OK`: Tareas obtenidas exitosamente.
    - `404 Not Found`: No hay tareas asignadas a los miembros del equipo.

- **Obtener Tareas por Usuario:**
  - **Descripción:** Permite obtener todas las tareas asignadas a un usuario.
  - **Endpoint:** `/api/tareas/{IdEmpleado}`
  - **Método HTTP:** GET
  - **Respuestas:**
    - `200 OK`: Tareas obtenidas exitosamente.
    - `404 Not Found`: Usuario no encontrado o sin tareas asignadas.

### Servicio de contacto con Telegram

- **Enviar Mensaje:**
  - **Descripción:** Permite enviar mensajes a través de Telegram.
  - **Endpoint:** `/api/telegram/send`
  - **Método HTTP:** POST
  - **Respuestas:**
    - `200 OK`: Mensaje enviado exitosamente.
    - `404 Bad Request`: Error en el envío.

- **Recibir Mensaje:**
  - **Descripción:** Permite recibir mensajes a través de Telegram.
  - **Endpoint:** `/api/telegram/get`
  - **Método HTTP:** PUT
  - **Respuestas:**
    - `200 OK`: Mensaje recibido exitosamente.
    - `404 Bad Request`: Error en el recibimiento.
