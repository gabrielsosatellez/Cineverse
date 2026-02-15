# CineVerse

CineVerse es una plataforma completa de gestión de cine compuesta por:

- Aplicación web para administración (React)  
- Backend en Spring Boot  
- Aplicación móvil Android para clientes  

Permite la gestión de películas, salas y sesiones, así como la compra de entradas con selección de asientos en tiempo real. Además, incluye un sistema de chat en tiempo real entre clientes y personal del cine.

---

## Estructura del proyecto

CineVerse/  
├── springboot/                  Backend Spring Boot  
├── Cineverse_web/               Aplicación web (React)  
│   └── cineverse-web/           Proyecto React  
├── cineverse_app_movil/         Aplicación móvil Android  
└── README.md  

---

## Requisitos previos

Para ejecutar el proyecto en local es necesario tener instalado:

- Java JDK 17  
- MySQL Server + MySQL Workbench  
- Node.js (LTS)  
- Git  
- Android Studio  

---

## Despliegue del proyecto

### 1. Backend (Spring Boot)

El backend gestiona toda la lógica de negocio: usuarios, autenticación, películas, salas, sesiones, compra de entradas y chat en tiempo real.

1. Crear la base de datos en MySQL:

CREATE DATABASE cineverse_db;

2. Configurar la conexión a la base de datos en:

springboot/src/main/resources/application.properties

spring.datasource.url=jdbc:mysql://localhost:3306/cineverse_db  
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA  
spring.jpa.hibernate.ddl-auto=update  

3. Compilar el proyecto:

mvn clean package o mvnw clean package (en Windows)

4. Ejecutar el backend:

java -jar target/cineverse-0.0.1-SNAPSHOT.jar  

El backend se ejecuta por defecto en:  
http://localhost:8080  

Es imprescindible que el backend esté en ejecución antes de iniciar la aplicación web o la aplicación móvil.

---

### 2. Aplicación Web (React)

La aplicación web está orientada a la administración del cine (personal y administradores).

1. Acceder a la carpeta del frontend:

cd Cineverse_web/cineverse-web  

2. Instalar dependencias:

npm install  

3. Ejecutar la aplicación en local:

npm run dev (App Escritorio)  
o  
npm start (App Web)  

La aplicación web estará disponible en el navegador en:  
http://localhost:3000  

Desde esta interfaz se puede acceder al panel de administración tras iniciar sesión con un usuario con rol EMPLOYEE o ADMIN.

---

### 3. Aplicación móvil (Android)

La aplicación móvil está orientada a los clientes del cine para consultar sesiones y comprar entradas.

1. Abrir la carpeta cineverse_app_movil en Android Studio.  
2. Verificar la URL del backend en la configuración de Retrofit:

BASE_URL = "http://10.0.2.2:8080"  

3. Ejecutar la aplicación en un emulador o en un dispositivo físico.

Para que la aplicación móvil funcione correctamente, el backend debe estar arrancado previamente.

---

## Funcionalidades y uso de la aplicación

### Usuarios

- Registro de usuarios:  
  Permite crear una cuenta de usuario desde la aplicación móvil o web.  

- Inicio de sesión:  
  Autenticación de usuarios mediante email y contraseña.  

- Cierre de sesión:  
  Permite cerrar la sesión activa.  

- Eliminación de cuenta:  
  El usuario puede eliminar su cuenta desde su perfil.  

---

### Gestión del cine (Aplicación Web)

Estas funcionalidades están disponibles para usuarios con rol EMPLOYEE o ADMIN:

- Gestión de películas:  
  Permite crear, editar, eliminar y listar películas en cartelera.  

- Gestión de salas:  
  Permite crear salas de cine y definir su capacidad.  

- Gestión de sesiones:  
  Permite asignar películas a salas en una fecha y hora concreta.

- Gestión de tickets:
  Permite cambiar los asientos ocupados a otros libres o liberarlos directamente.

- Gestión de usuarios y roles:  
  El administrador puede asignar roles (ADMIN, EMPLOYEE, USER).  

- Importación de películas desde API externa (SOLO ADMIN):  
  Permite importar automáticamente películas desde una API pública para poblar la base de datos.

---

### Compra de entradas (Aplicación móvil)

Estas funcionalidades están disponibles para los clientes desde la app móvil:

- Listado de sesiones:  
  El usuario puede consultar las sesiones disponibles por película y fecha.  

- Selección de asientos:  
  Se muestra el plano de la sala con los asientos libres y ocupados en tiempo real.  

- Carrito de compra:  
  El usuario puede añadir entradas al carrito antes de confirmar la compra.  

- Compra de entradas:  
  Finaliza la compra de las entradas seleccionadas y se genera un .pdf con las entradas obtenidas.

- Generación y visualización de tickets:  
  Tras la compra, el usuario puede consultar sus tickets desde la aplicación.  

---

### Comunicación en tiempo real

- Chat cliente–empleado:  
  Permite la comunicación directa entre los clientes (app móvil) y el personal del cine (aplicación web) mediante WebSockets. (Deben estar abiertos ambos chats, tanto la app movil como la app web)  

- Actualización en tiempo real de asientos:  
  Los asientos ocupados se actualizan en tiempo real para evitar reservas duplicadas. (Se simula en local para el usuario actual)

---

## Notas

- Los archivos generados automáticamente (node_modules, builds, APKs, ejecutables, etc.) no se incluyen en el repositorio.

---

## Credenciales de prueba

Para facilitar la evaluación del proyecto, se proporcionan las siguientes credenciales de ejemplo:

### Usuario Administrador (Web)
(Por seguridad los usuarios se crean por defecto como 'USER', solo el ADMIN tiene el poder de cambiar los roles, por lo tanto, crear el ADMIN desde la app web y actualizar el rol desde la base de datos:

UPDATE users
SET role = 'ADMIN'
WHERE email = 'admin@cineverse.com';

No crear el ADMIN directamente desde la base de datos, ya que la contraseña se guarda hasheada)

Email: admin@cineverse.com  
Contraseña: admin123  

Permite acceder al panel de administración de la aplicación web con permisos completos:
- Gestión de películas  
- Gestión de salas  
- Gestión de sesiones  
- Gestión de usuarios y roles  

---

### Usuario Empleado (Web)

Email: employee@cineverse.com  
Contraseña: employee123  

Permite acceder a la aplicación web con permisos de empleado:
- Gestión de películas  
- Gestión de salas  
- Gestión de sesiones  
- Atención al cliente mediante el chat en tiempo real  

---

### Usuario Cliente (App móvil)

Email: user@cineverse.com  
Contraseña: user123  

Permite acceder a la aplicación móvil para:
- Consultar sesiones  
- Seleccionar asientos  
- Comprar entradas  
- Ver tickets comprados  
- Usar el chat con el personal del cine  

---

