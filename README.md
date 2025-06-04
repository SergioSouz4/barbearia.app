Barbearia App - Sistema de Agendamentos
Sobre o Projeto
O Barbearia App é um sistema web completo para gerenciamento de agendamentos de barbearia. Desenvolvido com Java Spring Boot no backend e HTML5 + Bootstrap no frontend, este projeto implementa um sistema completo de agendamentos com autenticação de usuários, gerenciamento de clientes, barbeiros, serviços e horários.

Tecnologias Utilizadas
Backend
Java 17
Spring Boot 3.2.x
Spring Data JPA
Spring Security
MySQL
Lombok
Maven
Frontend
HTML5
CSS3
JavaScript
Bootstrap 5
Fetch API
Funcionalidades
Cadastro e autenticação de usuários
Gerenciamento de clientes
Gerenciamento de barbeiros
Cadastro e manutenção de serviços
Agendamento de horários
Confirmação e cancelamento de agendamentos
Dashboard administrativo
Relatórios de agendamentos
Como Executar o Projeto
Pré-requisitos
JDK 17+
Maven
MySQL
Git
Passos para Execução
Clone o repositório:
bash
Copiar

git clone https://github.com/seu-usuario/barbearia-app.git
cd barbearia-app
Configure o banco de dados MySQL:
sql
Copiar

CREATE DATABASE barbearia_db;
Configure as credenciais do banco de dados em src/main/resources/application.properties

Execute a aplicação:

bash
Copiar

mvn spring-boot:run
Acesse a aplicação em seu navegador:
http://localhost:8080
Estrutura do Projeto
📁 barbearia-app/
├── 📁 src/main/java/com/barbearia/
│   ├── 📁 config/         # Configurações do Spring
│   ├── 📁 controller/     # Controladores REST
│   ├── 📁 dto/            # Objetos de Transferência de Dados
│   ├── 📁 exception/      # Exceções personalizadas
│   ├── 📁 model/          # Entidades JPA
│   ├── 📁 repository/     # Repositórios JPA
│   ├── 📁 service/        # Camada de serviço
│   └── BarbeariaApplication.java  # Classe principal
├── 📁 src/main/resources/
│   ├── application.properties     # Configurações da aplicação
│   ├── 📁 static/                 # Recursos estáticos (CSS, JS)
│   └── 📁 templates/              # Templates HTML
└── pom.xml                        # Dependências Maven
API Endpoints
Autenticação
POST /api/auth/login - Login de usuário
POST /api/auth/register - Registro de novo usuário
Usuários
GET /api/usuarios - Listar todos os usuários
GET /api/usuarios/{id} - Buscar usuário por ID
POST /api/usuarios - Criar novo usuário
PUT /api/usuarios/{id} - Atualizar usuário
DELETE /api/usuarios/{id} - Excluir usuário
Clientes
GET /api/clientes - Listar todos os clientes
GET /api/clientes/{id} - Buscar cliente por ID
POST /api/clientes - Criar novo cliente
PUT /api/clientes/{id} - Atualizar cliente
DELETE /api/clientes/{id} - Excluir cliente
Barbeiros
GET /api/barbeiros - Listar todos os barbeiros
GET /api/barbeiros/{id} - Buscar barbeiro por ID
POST /api/barbeiros - Criar novo barbeiro
PUT /api/barbeiros/{id} -
