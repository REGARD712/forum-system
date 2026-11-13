# 基于 Spring Boot 的论坛系统

[English](README.en.md)

一个以 Spring Boot、Spring MVC、MyBatis 和 MySQL 为核心的论坛系统，实现用户、版块、帖子、回复和站内信等完整业务流程。项目适合用于学习 Java Web 分层开发、数据库设计、事务管理、登录鉴权以及前后端异步交互。

## 项目简介

本项目采用前后端分离的开发方式：前端由 HTML、CSS、JavaScript 和 jQuery AJAX 构成，后端通过 Spring MVC 提供 JSON 接口。静态页面与后端代码目前由同一个 Spring Boot 应用承载和部署，并非两个独立部署的服务。

项目重点不仅是实现增删改查，还包括以下业务规则：

- 发帖时同步维护用户发帖数和版块帖子数。
- 回复时同步维护帖子回复数。
- 删除帖子时使用软删除，并回退相关计数。
- 回复站内信时同时更新原消息状态并生成新消息。
- 编辑、删除帖子以及修改消息状态时校验资源归属。

## 功能列表

| 模块 | 已实现功能 |
| --- | --- |
| 用户 | 注册、登录、退出、查看与修改个人资料、修改密码 |
| 版块 | 首页版块展示、排序展示、版块帖子数维护 |
| 帖子 | 发帖、按版块或用户查询、详情浏览、访问量统计、编辑、点赞计数、软删除 |
| 回复 | 一级回复、回复列表、回复数维护 |
| 站内信 | 发送、未读数、接收列表、标记已读、回复站内信 |
| 公共能力 | Session 登录态、登录拦截、统一响应、统一异常处理、Swagger 接口文档、Actuator 健康检查基础能力 |

## 技术栈

| 分类 | 技术 |
| --- | --- |
| 运行环境 | Java 8、Maven |
| 后端框架 | Spring Boot 2.7.6、Spring MVC |
| 持久层 | MyBatis、MySQL、Druid |
| 前端 | HTML、CSS、JavaScript、jQuery AJAX、Editor.md |
| 工程支撑 | Springfox Swagger、Spring Boot Actuator、Lombok |
| 认证方式 | HttpSession、Spring MVC HandlerInterceptor |

## 系统架构

```text
浏览器静态页面 / jQuery AJAX
            |
            v
Spring MVC Controller
            |
            v
Service（业务规则、资源归属校验、事务边界）
            |
            v
MyBatis Mapper（SQL 与关联对象映射）
            |
            v
MySQL
```

登录拦截器在 Controller 执行前统一检查登录态；全局异常处理器和统一响应结构负责规范接口返回。发帖、回复、删帖和站内信回复等多步写入操作通过声明式事务保证整体提交或回滚。

## 项目结构

```text
forum-system/                         # 仓库根目录
├── forum-system/                     # Spring Boot 应用
│   ├── src/main/java/com/me/forum/
│   │   ├── common/                   # 统一响应与结果码
│   │   ├── config/                   # 应用配置
│   │   ├── controller/               # Web 接口
│   │   ├── dao/                      # MyBatis Mapper 接口
│   │   ├── exception/                # 业务异常与全局异常处理
│   │   ├── interceptor/              # 登录拦截器
│   │   ├── model/                    # 数据模型
│   │   ├── services/                 # 业务接口与实现
│   │   └── utils/                    # 工具类
│   ├── src/main/resources/
│   │   ├── mapper/                   # MyBatis XML 映射文件
│   │   ├── static/                   # 静态页面与前端资源
│   │   └── application.yml           # 应用配置
│   ├── src/forum_db.sql              # 数据库建表与初始数据脚本
│   └── pom.xml                       # Maven 配置
├── README.md                         # 中文说明
└── README.en.md                      # 英文说明
```

## 快速开始

### 1. 环境准备

请准备以下环境：

- JDK 8
- Maven 3.x
- 可用的 MySQL 服务

### 2. 克隆项目

```bash
git clone https://gitee.com/rae-gard712/forum-system.git
cd forum-system/forum-system
```

### 3. 初始化数据库

在 MySQL 客户端或数据库管理工具中执行 [forum_db.sql](forum-system/src/forum_db.sql)。

脚本会创建五张核心表：

| 表名 | 用途 |
| --- | --- |
| `t_user` | 用户资料、登录信息、发帖数和状态 |
| `t_board` | 版块信息、排序和帖子数 |
| `t_article` | 帖子内容、作者、所属版块及互动计数 |
| `t_article_reply` | 帖子一级回复及预留的回复关联字段 |
| `t_message` | 站内信发送者、接收者、内容和读取状态 |

**注意：该脚本会删除并重新创建 `forum_db` 数据库。执行前请确认其中没有需要保留的数据。**

### 4. 修改本地配置

根据本地环境修改 [application.yml](forum-system/src/main/resources/application.yml)：

- `spring.datasource.url`：MySQL 地址、端口和数据库名。
- `spring.datasource.username`：数据库用户名。
- `spring.datasource.password`：数据库密码。
- `server.port`：服务端口，当前配置为 `27271`。
- `logging.file.path`：日志目录，需确保当前用户具有写权限。

请不要将真实数据库密码等敏感配置提交到公开仓库。

### 5. 启动应用

在 `forum-system/forum-system` 目录执行：

```bash
mvn spring-boot:run
```

也可以先打包，再运行 JAR：

```bash
mvn clean package
java -jar target/forum-system-0.0.1-SNAPSHOT.jar
```

### 6. 访问系统

服务启动后，根据实际端口访问：

- 系统首页：<http://localhost:27271/>
- Swagger 接口文档：<http://localhost:27271/swagger-ui/index.html>
- 健康检查：<http://localhost:27271/actuator/health>

首次使用时，可以先注册账号并登录，再选择版块发布帖子、回复帖子或发送站内信。

## 工程设计亮点

### 事务保证业务一致性

发帖、回复、删帖和站内信回复都涉及多行或多张表的修改。相关 Service 方法通过 `@Transactional` 划定事务边界，避免只完成部分操作而留下不一致的数据。

### 认证与资源授权分离

登录拦截器负责判断用户是否登录，具体业务操作还会校验作者或消息接收者身份，避免“已登录即可操作任意资源”的横向越权问题。

### 统一接口行为

通过 `AppResult`、`ResultCode`、自定义业务异常和全局异常处理器统一响应结构与错误出口，减少 Controller 中的重复逻辑。

### 面向展示需求组装数据

使用 MyBatis `resultMap` 和 `association` 映射帖子—作者—版块、回复—用户、消息—发送者等关联数据，使后端返回结果更贴近页面展示模型。

### 区分业务状态与删除状态

通过 `state` 和 `deleteState` 分别表达业务状态与逻辑删除状态。软删除保留历史数据，但相关查询也需要遵守统一的删除过滤规则。

## 当前边界与后续方向

本项目目前以学习和单实例运行为主，以下能力不应视为已经完成：

- 当前仅实现一级回复，尚未实现完整的多级楼中楼。
- 点赞仅保存累计次数，尚无用户点赞关系、重复点赞限制和取消点赞。
- 用户模型包含管理员标识，但没有完整的后台管理功能。
- 列表分页、索引和并发计数仍有完善空间。
- 本地 Session 在多实例部署时需要共享会话或重新设计认证方案。
- 生产使用前需要升级密码哈希方案，并清理密码等敏感信息日志。

后续演进应优先保证正确性、安全性和可测试性，再根据真实负载评估缓存、消息队列、搜索服务或微服务，而不是单纯堆叠技术组件。

## 参与贡献

1. Fork 本仓库。
2. 创建功能分支，例如 `feature/update-readme`。
3. 提交修改并推送到自己的仓库。
4. 创建 Pull Request，说明修改内容和验证方式。

## 相关链接

- Gitee 仓库：<https://gitee.com/rae-gard712/forum-system>
- 数据库脚本：[forum_db.sql](forum-system/src/forum_db.sql)
- Maven 配置：[pom.xml](forum-system/pom.xml)
