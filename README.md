# Task Management API - Dokumentation

## ✨ Projektübersicht
Die **Task Management API** ist eine RESTful-API, die es ermöglicht, Aufgaben zu erstellen, zu aktualisieren, zu löschen und abzurufen. Die API unterstützt eine **JWT-basierte Authentifizierung** mit den Rollen **USER** und **ADMIN**.

---

## 🚿 Installation und Ausführung

### **1. Voraussetzungen**
- Java 17+
- Gradle
- PostgreSQL (oder H2 für Testzwecke)
- Docker (optional für eine schnelle Bereitstellung)

### **2. Projekt klonen**
```sh
 git clone https://github.com/avyunusemre/taskManagerApi.git
 cd taskManagerApi
```

### **3. Datenbankkonfiguration („application.properties“ anpassen)**
Falls PostgreSQL verwendet wird, stellen Sie sicher, dass die folgenden Werte korrekt gesetzt sind:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskdb
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```
Falls Sie H2 als In-Memory-Datenbank nutzen möchten:
```properties
spring.datasource.url=jdbc:h2:mem:taskdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

### **4. Anwendung starten**
Mit Gradle:
```sh
./gradlew bootRun
```

## 📝 Datenmodell

### **1. `User` (Benutzer)**
| Feld            | Typ | Beschreibung                             |
|-----------------|-----|------------------------------------------|
| `id`            | `Long` | Eindeutige ID des Benutzers              |
| `username`      | `String` | Benutzername (eindeutig)                 |
| `password`      | `String` | Verschlüsseltes Passwort                 |
| `email`         | `String` | Email                                    |
| `refresh token` | `String` | Refresh token                    |
| `role`          | `Enum` | Rolle des Benutzers (`USER` oder `ADMIN`) |

### **2. `Task` (Aufgabe)**
| Feld | Typ | Beschreibung |
|------|-----|--------------|
| `id` | `Long` | Eindeutige ID der Aufgabe |
| `title` | `String` | Titel der Aufgabe |
| `description` | `String` | Beschreibung der Aufgabe |
| `status` | `Enum` | Status (`PENDING`, `IN_PROGRESS`, `COMPLETED`) |
| `user_id` | `Long` | Zugeordneter Benutzer |

### **3. Beziehungen**
- Ein **User** kann mehrere **Tasks** haben (1:N Beziehung).
- Nur **Admins** können Tasks löschen.

---

## 🔧 API Endpunkte
Die API verwendet **JWT-Authentifizierung**. Alle geschützten Endpunkte erfordern einen **Bearer Token** im Header.

### **1. Authentifizierung**
| Methode | Endpunkt | Beschreibung |
|---------|---------|--------------|
| `POST` | `/api/auth/register` | Benutzer registrieren |
| `POST` | `/api/auth/login` | JWT-Token abrufen |
| `POST` | `/api/auth/refresh` | Token erneuern |

### **2. Task-Management**
| Methode | Endpunkt | Beschreibung |
|---------|---------|--------------|
| `POST` | `/api/tasks` | Neue Aufgabe erstellen |
| `GET` | `/api/tasks/{id}` | Einzelne Aufgabe abrufen |
| `PUT` | `/api/tasks/{id}` | Aufgabe aktualisieren |
| `DELETE` | `/api/tasks/{id}` | Aufgabe löschen (**Admin erforderlich**) |
| `GET` | `/api/tasks?status=completed` | Aufgaben nach Status filtern |

---

## 📈 API Dokumentation (Swagger)
Nach dem Starten der Anwendung ist die API-Dokumentation unter **Swagger UI** erreichbar:
```
http://localhost:8080/swagger-ui/index.html
```

## 🔗 WebSocket Integration
Die API unterstützt **WebSockets** für Echtzeit-Benachrichtigungen, wenn eine Aufgabe aktualisiert wird.

| Endpunkt | Beschreibung |
|----------|-------------|
| `ws://localhost:8080/ws` | WebSocket-Handshake |
| `/topic/tasks` | Broadcast für alle Benutzer |
| `/user/queue/reply` | Private Nachrichten für Benutzer |

---

## ✅ To-Do / Mögliche Erweiterungen
- **Rollenbasierte Zugriffskontrolle verbessern**
- **Rate-Limiting hinzufügen, um API-Missbrauch zu verhindern**
- **Docker Compose Setup mit PostgreSQL bereitstellen**
- **Weitere Tests (Unit & Integration Tests) ausbauen**

---

## 🌟 Autor
Dieses Projekt wurde von **Yunus Kaya** entwickelt. Bei Fragen oder Verbesserungsvorschlägen gerne einen **Issue** im Repository erstellen.

---

**Viel Spaß beim Nutzen der API! 🚀**

