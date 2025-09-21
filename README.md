# 📘 Projeto: API Escolar (Quarkus + Keycloak)

## 🚀 Stack

- **Java 21**
- **Quarkus** (RESTEasy + OIDC)
- **Keycloak** (autenticação e autorização)
- **PostgreSQL** (persistência da aplicação)
- **Docker Compose** (opcional para subir Keycloak e banco)

---

## 🔐 Autenticação e Autorização

- **Keycloak** é o _Identity Provider (IdP)_ central.
- Um **realm** único: `school-realm`.
- Roles criadas:
  - `administrador`
  - `coordenador`
  - `professor`
  - `aluno`
- API protegida com **JWT** emitido pelo Keycloak.
- Controle de acesso feito via `@RolesAllowed`.

Exemplo:

```java
@GET
@Path("/admin")
@RolesAllowed({"administrador"})
public String adminOnly() {
    return "Acesso restrito para administradores!";
}
```

## ▶️ Como usar

### 1. Entrar na pasta `database`

Acesse a pasta que contém o `docker-compose.yml`:

```bash
cd database
```

### 2. Rode o docker

Isso irá rodar o container do keycloaker com o postgres.

```bash
docker compose up -d
```

### 3. Volte a pasta do projeto

```bash
cd ..
```

### 3. Rode a aplicação

```bash
& ./mvnw.cmd quarkus:dev -f ./pom.xml
```
