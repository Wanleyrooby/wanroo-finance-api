# 💰 Wanroo Finance API

API REST para gerenciamento financeiro pessoal desenvolvida com Java e Spring Boot.

## 🚀 Tecnologias

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT Authentication
- MySQL
- Flyway
- Swagger/OpenAPI
- Maven
- Lombok

---

## 📋 Funcionalidades

### Autenticação

- Registro de usuários
- Login com JWT
- Controle de acesso por Roles (USER e ADMIN)

### Usuários

- Visualizar perfil do usuário autenticado
- Atualizar perfil do usuário autenticado
- Listar usuários (ADMIN)
- Buscar usuário por ID (ADMIN)

### Segurança

- Autenticação Stateless com JWT
- Senhas criptografadas com BCrypt
- Rotas protegidas com Spring Security

---

## 🏗️ Arquitetura

```text
src/main/java/com/wanroo/finance
├── controller
├── dto
├── entity
├── mapper
├── repository
├── security
├── service
└── exception
```

---

## ⚙️ Pré-requisitos

- Java 21+
- Maven 3.9+
- MySQL 8+
- Git

---

## 🗄️ Configuração do Banco

Crie um banco de dados MySQL:

```sql
CREATE DATABASE wanroo_finances;
```

Configure o arquivo `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/wanroo_finances
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

---

## ▶️ Executando o Projeto

Clone o repositório:

```bash
git clone https://github.com/SEU-USUARIO/wanroo-finance-api.git
```

Entre na pasta:

```bash
cd wanroo-finance-api
```

Execute a aplicação:

```bash
mvn spring-boot:run
```

---

## 📚 Documentação da API

Swagger:

```text
http://localhost:8080/swagger-ui.html
```

---

## 🔐 Endpoints Principais

### Auth

```http
POST /auth/register
POST /auth/login
```

### Usuário

```http
GET  /users/me
PUT  /users/me
```

### Administração

```http
GET /users
GET /users/{id}
```

---

## 📌 Próximas Funcionalidades

- Categorias
- Receitas
- Despesas
- Dashboard Financeiro
- Relatórios
- Testes Automatizados

---

## 👨‍💻 Autor

**Wanley Rooby Alexis**