# CineVerse

CineVerse es una plataforma completa de gestión de cine compuesta por:

- Aplicación web para administración (React)  
- Backend en Spring Boot  
- Aplicación móvil Android para clientes  

Permite la gestión de películas, salas, sesiones y la compra de entradas con selección de asientos en tiempo real, además de un sistema de chat entre clientes y personal del cine.

---

## Estructura del proyecto

CineVerse/  
 ├── springboot/                 Backend Spring Boot  
 ├── Cineverse_web/              Aplicación web (React)  
 │    └── cineverse-web/         Proyecto React  
 ├── cineverse_app_movil/        Aplicación móvil Android  
 └── README.md  

---

## Requisitos previos

- Java JDK 17  
- MySQL Server + MySQL Workbench  
- Node.js (LTS)  
- Git  
- Android Studio  

---

## Despliegue del proyecto

### Backend (Spring Boot)

1. Crear la base de datos en MySQL:

CREATE DATABASE cineverse_db;

2. Configurar la base de datos en `springboot/src/main/resources/application.properties`:

spring.datasource.url=jdbc:mysql://localhost:3306/cineverse_db  
spring.datasource.username=root  
spring.datasource.password=TU_CONTRASEÑA  
spring.jpa.hibernate.ddl-auto=update  

3. Compilar el proyecto:

mvn clean package  

4. Ejecutar el backend:

java -jar target/cineverse-0.0.1-SNAPSHOT.jar  

El backend se ejecuta en:  
http://localhost:8080  

---

### Aplicación Web (React)

1. Acceder a la carpeta del frontend:

cd Cineverse_web/cineverse-web  

2. Instalar dependencias:

npm install  

3. Ejecutar en local:

npm run dev o npm start

Disponible en:  
http://localhost:3000  

---

### Aplicación móvil (Android)

1. Abrir la carpeta `cineverse_app_movil` en Android Studio  
2. Verificar la URL del backend en la configuración de Retrofit:

BASE_URL = "http://10.0.2.2:8080"  

3. Ejecutar la aplicación en un emulador o dispositivo físico  

---

## Funcionalidades

Usuarios  
- Registro de usuarios  
- Inicio de sesión  
- Cierre de sesión  
- Eliminación de cuenta  

Gestión del cine (Web)  
- Gestión de películas  
- Gestión de salas  
- Gestión de sesiones  
- Gestión de usuarios por roles (ADMIN / EMPLOYEE / USER)  
- Importación de películas desde API externa  

Compra de entradas (App móvil)  
- Listado de sesiones  
- Selección de asientos  
- Carrito de compra  
- Compra de entradas  
- Generación de tickets  
- Visualización de tickets comprados  

Comunicación en tiempo real  
- Chat cliente–empleado con WebSockets  
- Actualización en tiempo real de asientos ocupados  

---

## Notas

- Los archivos generados automáticamente (node_modules, builds, APKs, etc.) no se incluyen en el repositorio.  
- El backend debe estar arrancado antes de iniciar la aplicación web o móvil para que las llamadas a la API funcionen correctamente.

