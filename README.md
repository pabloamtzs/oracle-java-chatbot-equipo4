# Oracle Java Chatbot Equipo 4
Repositorio del proyecto Oracle Java Chatbot del Equipo 4 para la materia de Desarrollo e implantación de sistemas de software


## Actividad de Aprendizaje 2 

Siguiendo los lineamientos que hemos estado trabajando a lo largo del semestre con el proyecto del reto y las tecnologías que nos han dado a estudiar y trabajar “workshops”, llegué a los siguientes puntos de trabajo y componentes principales:

## Requerimientos del proyecto

### Backend:

- **Lenguaje de programación:** Java.
- **Framework de desarrollo:** Spring Boot.
- **Base de datos:** Oracle Autonomous Database.

### Frontend:

- **Integración con Telegram API.**
- **Framework web para la interfaz de administración:** Angular o React.

## Utilidad de los Diversos Frameworks de Desarrollo

**Spring Boot:** Este framework de aplicación Java ofrece una estructura y configuración predefinidas que agilizan el desarrollo de aplicaciones independientes y basadas en producción. Su entorno preconfigurado simplifica el proceso de desarrollo e implementación.

**Angular o React:** Ambos frameworks frontend son ampliamente reconocidos por su capacidad para desarrollar interfaces de usuario interactivas y de una sola página. Proporcionan un enfoque estructurado que facilita la creación de interfaces de usuario dinámicas y complejas.

## Bibliotecas o Frameworks Auxiliares Necesarios

- **Telegram API:** Esencial para la integración con Telegram y permite la interacción del ChatBot con los usuarios a través de la plataforma.
- **Oracle JDBC Driver:** Permite la conexión a la base de datos Oracle desde la aplicación Java.
- **Spring Data JPA:** Proporciona una capa de persistencia para la interacción con la base de datos en el backend.
- **Spring Security:** Garantiza la autenticación y autorización de usuarios en la aplicación.

## Selección y Justificación de las Herramientas Tecnológicas

### Backend:

- **Spring Boot:** Ofrece una base sólida y eficiente para el desarrollo de la lógica de negocio y la interacción con la base de datos Oracle. Además, proporciona soporte para la implementación de API RESTful.
- **Oracle Autonomous Database:** Proporciona una base de datos gestionada y escalable con características avanzadas de seguridad y rendimiento.

### Frontend:

- **Telegram API:** Permite la comunicación bidireccional entre el ChatBot y los usuarios a través de Telegram.
- **Angular o React:** La elección entre estos frameworks dependerá de las necesidades específicas de la interfaz de administración y las preferencias del equipo de desarrollo. Ambos son adecuados para crear interfaces de usuario robustas y dinámicas.


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
