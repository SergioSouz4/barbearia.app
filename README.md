# 💈 Sistema de Agendamento para Barbearia

Um sistema web completo de agendamento para barbearias, desenvolvido com **Spring Boot** (backend) e preparado para integração com **Angular** (frontend).

## �� **Visão Geral**

Sistema responsivo de agendamento que permite o gerenciamento completo de uma barbearia, incluindo clientes, barbeiros, serviços, horários e comissões. Acesso via navegador (desktop, tablet ou celular) com diferentes perfis de usuário.

## 👥 **Perfis de Usuário**

### 🧑‍💼 **Cliente**
- Visualiza perfil da barbearia (serviços, preços, barbeiros)
- Agenda horários com barbeiros específicos
- Acompanha histórico de agendamentos
- Cancela agendamentos futuros
- Visualiza horários disponíveis em tempo real

### ✂️ **Barbeiro**
- Visualiza sua própria agenda
- Confirma e realiza atendimentos
- Marca clientes como "não compareceu"
- Acessa estatísticas pessoais (atendimentos, comissões)
- Gerencia horários de funcionamento

### 👨‍💼 **Administrador**
- Gerencia barbeiros e seus horários
- Cadastra e edita serviços e preços
- Visualiza agenda geral da barbearia
- Acessa relatórios financeiros completos
- Gerencia comissões dos barbeiros
- Cadastra clientes manualmente

## 🛠️ **Tecnologias Utilizadas**

### **Backend**
- **Java 17+**
- **Spring Boot 3.x**
- **Spring Security** (Autenticação JWT)
- **Spring Data JPA** (Persistência)
- **MySQL** (Banco de dados)
- **Swagger/OpenAPI** (Documentação da API)
- **Maven** (Gerenciamento de dependências)

### **Frontend** (Em desenvolvimento)
- **Angular 17+**
- **Angular Material** (UI responsiva)
- **TypeScript**
- **RxJS** (Programação reativa)

## 🏗️ **Arquitetura do Sistema**
┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐ │ Frontend │ │ Backend │ │ Banco de │ │ (Angular) │◄──►│ (Spring Boot) │◄──►│ Dados (MySQL) │ │ │ │ │ │ │ │ • Angular │ │ • REST API │ │ • Entidades │ │ • Material UI │ │ • JWT Auth │ │ • Relacionamentos│ │ • Responsivo │ │ • Validações │ │ • Índices │ └─────────────────┘ └─────────────────┘ └─────────────────┘


## 📊 **Modelo de Dados**

### **Entidades Principais**

```mermaid
erDiagram
    USUARIO ||--o{ CLIENTE : possui
    USUARIO ||--o{ BARBEIRO : possui
    CLIENTE ||--o{ AGENDAMENTO : faz
    BARBEIRO ||--o{ AGENDAMENTO : atende
    BARBEIRO ||--o{ HORARIO_FUNCIONAMENTO : tem
    BARBEIRO }o--o{ SERVICO : oferece
    SERVICO ||--o{ AGENDAMENTO : inclui
    AGENDAMENTO ||--o| COMISSAO : gera
    BARBEIRO ||--o{ COMISSAO : recebe
Relacionamentos
Cliente ↔ Agendamento (1:N)
Barbeiro ↔ Agendamento (1:N)
Barbeiro ↔ Serviços (N:N)
Agendamento ↔ Comissão (1:1)
Barbeiro ↔ Horário de Funcionamento (1:N)
🚀 Funcionalidades Implementadas
✅ Autenticação e Autorização
 Login com JWT
 Cadastro de clientes
 Controle de perfis (Cliente/Barbeiro/Admin)
 Middleware de autenticação
 Proteção de rotas por perfil
✅ Gestão de Clientes
 CRUD completo de clientes
 Busca por nome/telefone
 Histórico de agendamentos
 Ativação/desativação
✅ Gestão de Barbeiros
 CRUD completo de barbeiros
 Associação com serviços oferecidos
 Configuração de percentual de comissão
 Horários de funcionamento personalizados
✅ Gestão de Serviços
 CRUD completo de serviços
 Preços e duração configuráveis
 Associação com barbeiros
✅ Sistema de Agendamentos
 Criação de agendamentos
 Validação de conflitos de horário
 Verificação de disponibilidade
 Status: Agendado → Confirmado → Realizado
 Cancelamento com motivo
 Marcação de "não compareceu"
✅ Horários de Funcionamento
 Configuração por barbeiro e dia da semana
 Horários personalizados
 Criação de horários padrão
 Validação de disponibilidade
✅ Sistema de Comissões
 Cálculo automático de comissões
 Controle de pagamentos
 Relatórios por barbeiro e período
 Pagamento em lote
✅ Relatórios e Estatísticas
 Relatório geral da barbearia
 Relatórios por barbeiro
 Relatórios financeiros
 Taxa de comparecimento
 Ticket médio
 Estatísticas em tempo real
📋 Endpoints da API
🔐 Autenticação
http
Copiar

POST   /api/auth/login                    # Login
POST   /api/auth/cadastro/cliente         # Cadastro de cliente
GET    /api/auth/me                       # Dados do usuário logado
👥 Clientes
http
Copiar

GET    /api/clientes                      # Listar todos (Admin)
POST   /api/clientes                      # Criar cliente (Admin)
GET    /api/clientes/{id}                 # Buscar por ID
PUT    /api/clientes/{id}                 # Atualizar cliente
DELETE /api/clientes/{id}                 # Desativar cliente
GET    /api/clientes/nome/{nome}          # Buscar por nome
✂️ Barbeiros
http
Copiar

GET    /api/barbeiros                     # Listar todos
POST   /api/barbeiros                     # Criar barbeiro (Admin)
GET    /api/barbeiros/{id}                # Buscar por ID
PUT    /api/barbeiros/{id}                # Atualizar barbeiro
DELETE /api/barbeiros/{id}                # Desativar barbeiro
GET    /api/barbeiros/servico/{id}        # Buscar por serviço
💼 Serviços
http
Copiar

GET    /api/servicos                      # Listar todos (público)
POST   /api/servicos                      # Criar serviço (Admin)
GET    /api/servicos/{id}                 # Buscar por ID
PUT    /api/servicos/{id}                 # Atualizar serviço
DELETE /api/servicos/{id}                 # Desativar serviço
📅 Agendamentos
http
Copiar

GET    /api/agendamentos                  # Listar todos (Admin)
POST   /api/agendamentos                  # Criar agendamento
GET    /api/agendamentos/cliente/{id}     # Por cliente
GET    /api/agendamentos/barbeiro/{id}    # Por barbeiro
PUT    /api/agendamentos/{id}/confirmar   # Confirmar
PUT    /api/agendamentos/{id}/realizar    # Realizar
PUT    /api/agendamentos/{id}/cancelar    # Cancelar
POST   /api/agendamentos/disponibilidade  # Horários disponíveis
⏰ Horários de Funcionamento
http
Copiar

GET    /api/horarios-funcionamento/barbeiro/{id}  # Por barbeiro
POST   /api/horarios-funcionamento               # Criar horário
PUT    /api/horarios-funcionamento/{id}          # Atualizar
DELETE /api/horarios-funcionamento/{id}          # Desativar
POST   /api/horarios-funcionamento/barbeiro/{id}/padrao  # Criar padrão
💰 Comissões
http
Copiar

GET    /api/comissoes/barbeiro/{id}       # Por barbeiro
GET    /api/comissoes/nao-pagas/{id}      # Não pagas por barbeiro
PUT    /api/comissoes/{id}/pagar          # Marcar como pago
PUT    /api/comissoes/pagar-lote          # Pagamento em lote
📊 Relatórios
http
Copiar

GET    /api/relatorios/geral              # Relatório geral
GET    /api/relatorios/barbeiro/{id}      # Por barbeiro
GET    /api/relatorios/financeiro         # Financeiro
GET    /api/relatorios/estatisticas       # Estatísticas rápidas
🔧 Configuração e Instalação
Pré-requisitos
Java 17+
Maven 3.6+
MySQL 8.0+
Git
1. Clone o repositório
bash
Copiar

git clone https://github.com/seu-usuario/barbearia-app.git
cd barbearia-app
2. Configure o banco de dados
sql
Copiar

CREATE DATABASE barbearia_db;
CREATE USER 'barbearia_user'@'localhost' IDENTIFIED BY 'sua_senha';
GRANT ALL PRIVILEGES ON barbearia_db.* TO 'barbearia_user'@'localhost';
3. Configure o application.properties
properties
Copiar

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/barbearia_db
spring.datasource.username=barbearia_user
spring.datasource.password=sua_senha

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secret=minhachavesecretaparajwtquedevetersuficientescaracteresparasersegura
jwt.expiration=86400000

# Server
server.port=8080
4. Execute a aplicação
bash
Copiar

mvn clean install
mvn spring-boot:run
5. Acesse a documentação
Swagger UI: 
localhost
API Docs: 
localhost
🧪 Testando a API
1. Cadastrar um cliente
bash
Copiar

curl -X POST http://localhost:8080/api/auth/cadastro/cliente \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "telefone": "(11) 99999-9999",
    "dataNascimento": "1990-01-01",
    "senha": "123456",
    "confirmarSenha": "123456"
  }'
2. Fazer login
bash
Copiar

curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "senha": "123456"
  }'
3. Usar o token retornado
bash
Copiar

curl -X GET http://localhost:8080/api/servicos \
  -H "Authorization: Bearer SEU_TOKEN_JWT"
📁 Estrutura do Projeto
src/main/java/com/barbearia/barbearia_app/
├── config/                 # Configurações (Security, JWT, Swagger)
├── controller/             # Controllers REST
├── dto/                    # Data Transfer Objects
├── model/                  # Entidades JPA
│   └── enums/             # Enumerações
├── repository/             # Repositories JPA
├── service/               # Lógica de negócio
└── BarbeariaAppApplication.java
🔒 Segurança
JWT para autenticação stateless
BCrypt para hash de senhas
CORS configurado para frontend
Validação de entrada em todos os endpoints
Autorização baseada em perfis
Rate limiting (planejado)
📈 Métricas e Monitoramento
Swagger para documentação automática
Logs estruturados
Validações de entrada
Tratamento de exceções
Relatórios de performance
🚧 Roadmap
Próximas Funcionalidades
 Notificações por email/SMS
 Sistema de avaliações
 Integração com pagamentos
 App mobile nativo
 Dashboard analítico avançado
 Sistema de fidelidade
 Integração com redes sociais
 Backup automático
Melhorias Técnicas
 Cache com Redis
 Testes automatizados
 CI/CD pipeline
 Docker containerization
 Monitoramento com Prometheus
 Rate limiting
 Logs centralizados
🤝 Contribuição
Fork o projeto
Crie uma branch para sua feature (git checkout -b feature/AmazingFeature)
Commit suas mudanças (git commit -m 'Add some AmazingFeature')
Push para a branch (git push origin feature/AmazingFeature)
Abra um Pull Request
📄 Licença
Este projeto está licenciado sob a Licença MIT - veja o arquivo LICENSE para detalhes.

👨‍💻 Autor
Seu Nome

GitHub: 

github.com
LinkedIn: 

linkedin.com
Email: seu.email@exemplo.com
🙏 Agradecimentos
Spring Boot community
Angular team
MySQL team
Todos os contribuidores
⭐ Se este projeto te ajudou, deixe uma estrela!


## 🎯 **Principais melhorias no README:**

1. **✅ Visão completa** do sistema
2. **✅ Perfis de usuário** bem definidos
3. **✅ Arquitetura** explicada
4. **✅ Modelo de dados** com diagrama
5. **✅ Funcionalidades** detalhadas
6. **✅ Endpoints** completos
7. **✅ Guia de instalação** passo a passo
8. **✅ Exemplos** de uso da API
9. **✅ Estrutura** do projeto
10. **✅ Roadmap** de melhorias
