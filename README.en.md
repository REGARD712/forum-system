# Forum System Based on Spring Boot

[中文说明](README.md)

A forum system built with Spring Boot, Spring MVC, MyBatis, and MySQL. It implements end-to-end workflows for users, boards, articles, replies, and private messages. The project is intended for learning layered Java Web development, database modeling, transaction management, authentication, authorization, and asynchronous browser-server interaction.

## Overview

The project uses a front-end/back-end separated development style. The front end consists of HTML, CSS, JavaScript, and jQuery AJAX, while the back end exposes JSON APIs through Spring MVC. At present, the static pages and back-end code are served and deployed by the same Spring Boot application rather than as two independently deployed services.

Beyond basic CRUD operations, the project implements business rules such as:

- Updating the author's article count and the board's article count when publishing an article.
- Updating the article's reply count when adding a reply.
- Soft-deleting articles and rolling back the related counters.
- Updating the original message status and creating a new message when replying to a private message.
- Checking resource ownership when editing or deleting articles and when changing message states.

## Features

| Module | Implemented features |
| --- | --- |
| Users | Registration, login, logout, profile viewing and editing, password changes |
| Boards | Board list on the home page, ordered display, article count maintenance |
| Articles | Publishing, queries by board or user, details, visit counts, editing, like counts, soft deletion |
| Replies | First-level replies, reply lists, reply count maintenance |
| Private messages | Sending, unread counts, inbox lists, marking as read, replying |
| Shared capabilities | Session-based login, login interception, unified responses, global exception handling, Swagger API documentation, basic Actuator health checks |

## Technology Stack

| Category | Technologies |
| --- | --- |
| Runtime | Java 8, Maven |
| Back end | Spring Boot 2.7.6, Spring MVC |
| Persistence | MyBatis, MySQL, Druid |
| Front end | HTML, CSS, JavaScript, jQuery AJAX, Editor.md |
| Engineering support | Springfox Swagger, Spring Boot Actuator, Lombok |
| Authentication | HttpSession, Spring MVC HandlerInterceptor |

## Architecture

```text
Static browser pages / jQuery AJAX
            |
            v
Spring MVC Controller
            |
            v
Service (business rules, ownership checks, transaction boundaries)
            |
            v
MyBatis Mapper (SQL and associated-object mapping)
            |
            v
MySQL
```

A login interceptor checks the session before Controller execution. A global exception handler and a unified response structure standardize API responses. Multi-step writes for publishing, replying, deleting, and private-message replies use declarative transactions so that they commit or roll back as a whole.

## Project Structure

```text
forum-system/                         # Repository root
├── forum-system/                     # Spring Boot application
│   ├── src/main/java/com/me/forum/
│   │   ├── common/                   # Unified responses and result codes
│   │   ├── config/                   # Application configuration
│   │   ├── controller/               # Web APIs
│   │   ├── dao/                      # MyBatis Mapper interfaces
│   │   ├── exception/                # Business and global exception handling
│   │   ├── interceptor/              # Login interceptor
│   │   ├── model/                    # Data models
│   │   ├── services/                 # Business interfaces and implementations
│   │   └── utils/                    # Utilities
│   ├── src/main/resources/
│   │   ├── mapper/                   # MyBatis XML mappers
│   │   ├── static/                   # Static pages and front-end assets
│   │   └── application.yml           # Application configuration
│   ├── src/forum_db.sql              # Schema and seed-data script
│   └── pom.xml                       # Maven configuration
├── README.md                         # Chinese documentation
└── README.en.md                      # English documentation
```

## Getting Started

### 1. Prerequisites

Install and prepare:

- JDK 8
- Maven 3.x
- An available MySQL server

### 2. Clone the Repository

```bash
git clone https://gitee.com/rae-gard712/forum-system.git
cd forum-system/forum-system
```

### 3. Initialize the Database

Execute [forum_db.sql](forum-system/src/forum_db.sql) with a MySQL client or database administration tool.

The script creates five core tables:

| Table | Purpose |
| --- | --- |
| `t_user` | User profiles, credentials, article counts, and states |
| `t_board` | Board information, ordering, and article counts |
| `t_article` | Article content, authors, boards, and interaction counters |
| `t_article_reply` | First-level article replies and reserved reply-relation fields |
| `t_message` | Private-message senders, recipients, content, and read states |

**Warning: the script drops and recreates the `forum_db` database. Back up any data you need before running it.**

### 4. Configure the Local Environment

Update [application.yml](forum-system/src/main/resources/application.yml) for your environment:

- `spring.datasource.url`: MySQL host, port, and database name.
- `spring.datasource.username`: Database username.
- `spring.datasource.password`: Database password.
- `server.port`: Server port, currently configured as `27271`.
- `logging.file.path`: Log directory, which must be writable by the current user.

Do not commit real database passwords or other sensitive configuration to a public repository.

### 5. Run the Application

From the `forum-system/forum-system` directory, run:

```bash
mvn spring-boot:run
```

Alternatively, build and run the JAR:

```bash
mvn clean package
java -jar target/forum-system-0.0.1-SNAPSHOT.jar
```

### 6. Access the System

After startup, open the following URLs using your configured port:

- Home page: <http://localhost:27271/>
- Swagger API documentation: <http://localhost:27271/swagger-ui/index.html>
- Health check: <http://localhost:27271/actuator/health>

To try the main workflow, register an account, log in, select a board, publish an article, add a reply, or send a private message.

## Engineering Highlights

### Transactional Business Consistency

Publishing articles, adding replies, deleting articles, and replying to private messages all modify multiple rows or tables. The related Service methods use `@Transactional` to define transaction boundaries and prevent partially completed business operations.

### Separate Authentication and Resource Authorization

The login interceptor verifies whether a user is authenticated. Business operations additionally verify article authorship or message-recipient identity, preventing an authenticated user from operating on arbitrary resources.

### Consistent API Behavior

`AppResult`, `ResultCode`, custom business exceptions, and a global exception handler provide a consistent response structure and error-handling path while reducing repeated Controller logic.

### Data Mapping for Page Models

MyBatis `resultMap` and `association` mappings assemble article-author-board, reply-user, and message-sender relationships so that API responses better match the data required by the pages.

### Separate Business and Deletion States

The `state` and `deleteState` fields represent business states and logical deletion separately. Soft deletion preserves historical data, but all related queries must consistently apply the deletion filter.

## Current Limitations and Future Work

The project is currently intended for learning and single-instance use. The following capabilities should not be considered complete:

- Only first-level replies are implemented; nested reply threads are not complete.
- Likes are stored as counters only, without user-like relations, duplicate-like prevention, or unlike support.
- The user model contains an administrator flag, but there is no complete administration console.
- List pagination, indexing, and concurrent counter updates need further work.
- Local sessions require shared session storage or a redesigned authentication approach for multi-instance deployment.
- Before production use, password hashing must be upgraded and logs containing passwords or other sensitive data must be removed.

Future work should prioritize correctness, security, and testability. Caching, message queues, search services, or microservices should be introduced only in response to measured requirements rather than for technology stacking.

## Contributing

1. Fork the repository.
2. Create a feature branch, such as `feature/update-readme`.
3. Commit your changes and push the branch to your fork.
4. Open a Pull Request describing the changes and how they were verified.

## Links

- Gitee repository: <https://gitee.com/rae-gard712/forum-system>
- Database script: [forum_db.sql](forum-system/src/forum_db.sql)
- Maven configuration: [pom.xml](forum-system/pom.xml)
