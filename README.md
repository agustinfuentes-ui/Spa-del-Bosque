#  SPA del Bosque
Aplicación móvil desarrollada en **Android Studio con Kotlin y Jetpack Compose** 
para la gestión de servicios, reservas y visualización de información de un spa.

---

##  ¿Qué hace la aplicación?

**SPA del Bosque** es una aplicación móvil que permite a los usuarios:

- Navegar entre las secciones principales: **Inicio**, **Servicios**, **Blogs**, **Nosotros** y **Contacto**.
- Ver detalles de los servicios ofrecidos por el spa y sus precios.
- Agregar servicios al carrito y avanzar a una pantalla de compra.
- Iniciar sesión y administrar su **perfil de usuario**.
- Disfrutar de una interfaz moderna, intuitiva y **adaptable a diferentes tamaños de pantalla** (usando `calculateWindowSizeClass()`).

> El diseño visual se inspira en la identidad del spa, utilizando tonos verdes 
> y una composición limpia basada en **Material Design 3**.

---

##  Cómo ejecutar la aplicación

1. **Abrir el proyecto**
    - En Android Studio:  
      `File → Open → Spa-del-Bosque`

2. **Seleccionar dispositivo de prueba**
    - Usa un emulador con **API Level 34 o superior**, o un dispositivo físico con Android 12+.

3. **Ejecutar el proyecto**
    - Presiona  **Run 'app'** o utiliza el atajo `Shift + F10`.

> Si aparece alguna advertencia sobre versiones de Gradle o SDK, puede ignorarse siempre que la app compile correctamente.

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

---

##  Roles del equipo *(opcional)*

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

