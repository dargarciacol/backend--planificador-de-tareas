# Backend - Planificador de Tareas

API RESTful desarrollada en **Spring Boot** para la gestión e integración del Planificador de Tareas (Sprint 4). Cuenta con persistencia de datos en **PostgreSQL (Supabase)**, documentación interactiva con **OpenAPI/Swagger** y despliegue continuo en **Render**.

---

## 🔗 Links de Despliegue

* 🌐 **API Backend (Render):** `https://backend-planificador-de-tareas.onrender.com`
* 📚 **Documentación Swagger UI:** `https://backend-planificador-de-tareas.onrender.com/swagger-ui/index.html`

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 17 / 21
* **Framework:** Spring Boot 3.x (Spring Web, Spring Data JPA, Validation)
* **Base de Datos:** PostgreSQL (Cloud hosting en Supabase)
* **Documentación:** Springdoc OpenAPI / Swagger UI
* **Despliegue:** Render

---

## 📌 Endpoints de la API REST (`/api/tasks`)

| Método | Endpoint | Descripción | Estado HTTP |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/tasks` | Obtiene la lista completa de tareas registradas | `200 OK` |
| `POST` | `/api/tasks` | Crea una nueva tarea en la base de datos | `201 Created` |
| `PUT` | `/api/tasks/{id}` | Actualiza los datos o el estado de una tarea por ID | `200 OK` / `404 Not Found` |
| `DELETE` | `/api/tasks/{id}` | Elimina una tarea existente por su ID | `204 No Content` / `404 Not Found` |

---

## 📋 Estructura de Datos (DTO Request/Response)

```json
{
  "name": "Completar Sprint 4",
  "description": "Integrar Spring Boot con Supabase y Swagger",
  "dueDate": "2026-09-20",
  "status": "PENDING",
  "priority": "Alta"
}
El backend del **Planificador de Tareas** se encuentra completamente finalizado y funcional.

Actualmente cuenta con:

- ✅ API REST desarrollada con Spring Boot.
- ✅ Persistencia de datos con PostgreSQL mediante Supabase.
- ✅ Operaciones CRUD para la gestión de tareas.
- ✅ Validación de datos.
- ✅ Documentación interactiva mediante Swagger / OpenAPI.
- ✅ Despliegue en Render.
- ✅ Integración funcional con el frontend.
- ✅ Comunicación entre frontend y backend mediante API REST.
- ✅ Persistencia de la información en la base de datos.

### 🚀 Proyecto Finalizado

El backend se encuentra listo para ser utilizado junto con el frontend del **Planificador de Tareas**, permitiendo gestionar las tareas de forma persistente mediante una API REST desplegada en la nube.
git