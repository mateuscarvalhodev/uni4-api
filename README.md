# 📘 Projeto: API Escolar (Quarkus + Keycloak)

## 🚀 Stack Tecnológica

- **Java 21**
- **Quarkus** (RESTEasy + OIDC)
- **Keycloak** (autenticação e autorização)
- **PostgreSQL** (persistência da aplicação)
- **Docker Compose** (para subir Keycloak e banco de dados)

---

## 🔐 Autenticação e Autorização

- **Keycloak** é o _Identity Provider (IdP)_ central
- Um **realm** único: `school-realm`
- **Roles disponíveis:**
  - `administrador`
  - `coordenador`
  - `professor`
  - `aluno`
- API protegida com **JWT** emitido pelo Keycloak
- Controle de acesso implementado via `@RolesAllowed`

**Exemplo de uso:**

```java
@GET
@Path("/admin")
@RolesAllowed({"administrador"})
public String adminOnly() {
    return "Acesso restrito para administradores!";
}
```

---

## ▶️ Como usar

### 1. Entrar na pasta `database`

Acesse a pasta que contém o `docker-compose.yml`:

```bash
cd database
```

### 2. Executar o Docker Compose

Isso irá executar os containers do Keycloak com PostgreSQL:

```bash
docker compose up -d
```

### 3. Configurar o ambiente do Keycloak

#### 3.1. Acessar a interface do Keycloak

- Abra o navegador e acesse: `http://localhost:8081`
- Faça login com as credenciais de administrador:
  - **Username:** `admin`
  - **Password:** `admin`

#### 3.2. Selecionar o realm

- No canto superior esquerdo da tela, clique no dropdown do realm
- Mude de `master` para `school-realm`

#### 3.3. Configurar o client

1. Acesse a aba **Clients**
2. Clique no client **quarkus-app**
3. Na categoria **Settings**, vá para a subcategoria **Capability config**
4. Habilite a funcionalidade **Service accounts roles**
5. Clique em **Save**

#### 3.4. Atribuir roles ao service account

1. Vá para a seção **Service accounts roles**
2. Clique em **Assign role**
3. Selecione todas as roles disponíveis (para facilidade - em produção, selecione apenas as roles específicas necessárias)
4. Confirme a seleção
5. Volte para a seção **Settings** e salve novamente

### 4. Voltar à pasta do projeto

```bash
cd ..
```

### 5. Executar a aplicação

```bash
./mvnw.cmd quarkus:dev -f ./pom.xml
```

---

## 🔧 Notas Importantes

- Certifique-se de que as portas necessárias estão disponíveis (8001 para Keycloak, 8080 para a aplicação)
- O PostgreSQL será configurado automaticamente pelo Docker Compose
- Em ambiente de produção, configure roles específicas em vez de atribuir todas as roles ao service account
