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
 ├── cineverse_app_movil/        Aplicación móvil Android  
 └── README.md  

---

## Despliegue del proyecto

### Backend (Spring Boot)

1. Configurar la base de datos en `application.properties`:

spring.datasource.url=jdbc:mysql://localhost:3306/cineverse  
spring.datasource.username=usuario  
spring.datasource.password=contraseña  

2. Compilar el proyecto:

mvn clean package  

3. Ejecutar el backend:

java -jar target/cineverse-0.0.1-SNAPSHOT.jar  

El backend se ejecuta en:  
http://localhost:8080

---

### Aplicación Web (React)

1. Acceder a la carpeta:

cd Cineverse_web/cineverse_web  

2. Instalar dependencias:

npm install  

3. Ejecutar en local:

npm start  

Disponible en:  
http://localhost:3000

---

### Aplicación móvil (Android)

1. Abrir `cineverse_app_movil` en Android Studio  
2. Verificar la URL del backend en Retrofit:

BASE_URL = "http://10.0.2.2:8080"  

3. Ejecutar en emulador o dispositivo físico

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
- Generación de tickets en PDF  
- Visualización de tickets comprados  

Comunicación en tiempo real  
- Chat cliente–empleado con WebSockets  
- Actualización en tiempo real de asientos ocupados  

---

## Mejoras implementadas

- Generación automática de entradas en PDF  
- Chat en tiempo real  

---

## Notas

Los archivos binarios generados (instaladores, ejecutables y builds) no se incluyen en el repositorio debido a las limitaciones de tamaño de GitHub.  
El instalador de escritorio debe generarse localmente.

---

## 🎓 Proyecto académico

Proyecto desarrollado como parte del módulo de Desarrollo de Aplicaciones Multiplataforma (DAM).
