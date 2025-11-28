#  SPA del Bosque
Aplicación móvil desarrollada en **Android Studio con Kotlin y Jetpack Compose** 
para la gestión de servicios, reservas y visualización de información de un spa.

---

## Funcionalidades Implementadas

**SPA del Bosque** es una aplicación móvil que actualmente permite:

- **Autenticación Completa:**
    - **Registro de nuevos usuarios** con validaciones de formulario en tiempo real.
    - **Inicio de sesión** seguro contra un microservicio de backend.
    - **Cierre de sesión** y limpieza de los datos locales.
- **Gestión de Perfil de Usuario:**
    - **Visualización** de los datos del perfil (nombre, correo, teléfono, etc.).
    - **Edición del perfil**, permitiendo actualizar nombre, apellidos, teléfono, región, comuna y fecha de nacimiento.
- **Interfaz de Usuario Moderna:**
    - Navegación fluida entre pantallas usando `NavHost` de Jetpack Compose.
    - Componentes de UI construidos con **Material Design 3**.
    - Carrusel de imágenes en la pantalla de inicio.
    - Diseño responsivo básico que se adapta a diferentes tamaños de pantalla.

> El diseño visual se inspira en la identidad del spa, utilizando tonos verdes 
> y una composición limpia basada en **Material Design 3**.


---

## Endpoints Utilizados

La aplicación se comunica con una arquitectura de microservicios a través de un API Gateway.

**URL Base (ejemplo):** `http://192.168.1.13:8080/`

### Microservicio de Usuarios (`spa-user-service`)

- **`POST /api/v1/users/register`**
  - **Descripción:** Registra un nuevo usuario en el sistema.
  - **Uso en la app:** Se consume desde la pantalla de Registro.

- **`POST /api/v1/users/login`**
  - **Descripción:** Valida las credenciales de un usuario y devuelve sus datos y un token si son correctos.
  - **Uso en la app:** Se consume desde la pantalla de Inicio de Sesión.

- **`GET /api/v1/users/{id}`**
  - **Descripción:** Obtiene los datos completos de un usuario a partir de su ID.
  - **Uso en la app:** Se utiliza para cargar la información en la pantalla de Perfil.

- **`PUT /api/v1/users/{id}`**
  - **Descripción:** Actualiza los datos de un usuario existente.
  - **Uso en la app:** Se consume desde la pantalla de "Editar Perfil".

---

## Cómo Ejecutar el Proyecto

### 1. Prerequisitos
- Android Studio (versión Iguana o superior).
- JDK 17 o superior.
- XAMPP o un servidor MySQL para la base de datos.
- Un IDE de Java (ej. IntelliJ IDEA) para los microservicios.

### 2. Backend
1. Iniciar el servidor de base de datos **MySQL** desde XAMPP.
2. Levantar el microservicio **`spa-user-service`** desde tu IDE de Java (correrá en el puerto `8081`). 
3. Levantar el microservicio **`api-gateway`** (correrá en el puerto `8080`).(`https://github.com/Palvarezlara/spa-gateway.git`)

### 3. Frontend (App Móvil)

**A. Para ejecutar en el Emulador de Android Studio:**
1. Abre el archivo `app/src/main/java/com/example/spadelbosque/data/remote/ApiConfig.kt`.
2. Asegúrate de que la URL base apunte a la IP especial del emulador
3. Ejecuta la aplicación (`Shift + F10`) en tu emulador.

**B. Para ejecutar en un Teléfono Físico:**
1. Conecta tu computador y tu teléfono a la **misma red Wi-Fi**.
2. Averigua la dirección IP de tu computador (en Windows, usa `ipconfig` en la terminal).
3. Abre `app/src/main/java/com/example/spadelbosque/data/remote/ApiConfig.kt` y cambia la URL base por la IP de tu computador:
4. Abre `app/src/main/res/xml/network_security_config.xml` y asegúrate de que el dominio también apunte a tu IP.
5. Genera la APK firmada (`Build > Generate Signed Bundle / apk`) e instála en tu teléfono.

## APK Firmado y Archivo JKS

- APK: El archivo `app-release.apk`, listo para instalar, se encuentra en la raíz de este repositorio.
- Archivo JKS (Firma): El archivo de firma (`.jks`) se mantiene de forma privada y no se sube al repositorio por seguridad.
   
## Código Fuente Adicional (Microservicios)

- App Móvil: El código fuente completo se encuentra en este repositorio.
- Microservicios: El código de los servicios de backend se encuentra en los siguientes repositorios:◦User Service: [(https://github.com/Palvarezlara/spa-user-service.git)]◦API Gateway: [(https://github.com/Palvarezlara/spa-gateway.git)]

---

##Evidencia de Trabajo Colaborativo

- El historial de commits de este repositorio refleja las contribuciones individuales y el trabajo en equipo realizado durante el desarrollo del proyecto.


---

##  Tecnologías utilizadas

| Categoría                 | Tecnología / Librería |
|---------------------------|----------------------|
| Lenguaje principal        | **Kotlin 2.0.21** |
| Framework UI              | **Jetpack Compose (Material 3)** |
| Navegación                | `androidx.navigation.compose` |
| Arquitectura              | **MVVM** con `ViewModel` y `LiveData` |
| Persistencia local        | **Room** + **DataStore Preferences** |
| Corrutinas                | `kotlinx.coroutines` |
| Serialización             | **Gson** |
| Imágenes                  | **Coil Compose** |
| Inyección de dependencias | Clase `AppGraph` (manual DI) |
| Diseño responsivo         | `calculateWindowSizeClass()` (Material 3 Window Size) |
| Cliente HTTP              | **Retrofit 2** |

---

##  Roles del equipo 

| Integrante                 | 
|----------------------------|
| **Pamela Álvarez Lara**    |
| *Jorge Toledo Iporre*      |  
| *Agustin Fuentes Sandoval* |  

---

##  Notas adicionales

- El proyecto utiliza una estructura modular con carpetas separadas por vista y por tipo de componente.
- La navegación se gestiona desde el archivo `AppNavHost.kt`.
- El componente `MainShell` contiene el **Drawer lateral** y la **TopAppBar**.
- Se implementó **diseño responsivo** con `calculateWindowSizeClass()` para adaptar la UI a distintos tamaños de pantalla.
- Las dependencias se manejan mediante el archivo `libs.versions.toml` para asegurar compatibilidad y control de versiones.

---

###  Autoría
Proyecto desarrollado como parte de la asignatura  
**DSY1105 - Desarrollo de Aplicaciones Móviles**  
**Instituto Profesional Duoc UC – 2025**

---

