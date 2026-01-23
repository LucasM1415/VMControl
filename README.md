VMControl - Gerenciador de Máquinas Virtuais

📋 Descrição
VMControl é uma aplicação web completa para gerenciamento de máquinas virtuais, com backend em Spring Boot e frontend em Angular.

🏗️ Arquitetura
``` 
VMControl/
├── Backend/           # API Spring Boot
│   └── vmcontrol/
├── Frontend/          # Aplicação Angular
│   └── vmcontrol/
``` 
🚀 Pré-requisitos
Java 17 ou superior

Node.js 14+ e npm

PostgreSQL 12+

Angular 17

🗄️ Configuração do Banco de Dados
Inicie o serviço do PostgreSQL

Crie o banco de dados manualmente:

sql
``` 
CREATE DATABASE vmcontrol_db;
``` 
Verifique se o PostgreSQL está rodando na porta 5432

Pronto! O JPA criará as tabelas automaticamente

⚙️ Configuração do Backend (Spring Boot)
Navegue até o diretório do backend:

bash
``` 
cd Backend/vmcontrol
``` 
Execute o projeto de uma destas formas:

Via IDE: Importe como projeto Maven e execute VmcontrolApplication.java

Via linha de comando:

bash
``` 
./mvnw spring-boot:run
``` 
Backend rodando em: http://localhost:8080

🖥️ Configuração do Frontend (Angular)
Navegue até o diretório do frontend:

bash
``` 
cd Frontend/vmcontrol
``` 
Instale as dependências:

bash
``` 
npm install
``` 
Inicie a aplicação:

bash
``` 
npm start
``` 
Frontend rodando em: http://localhost:4200

📚 Documentação da API
Acesse a documentação interativa da API:

🔗 Swagger UI: ``` http://localhost:8080/api/swagger-ui/index.html#``` 

🔧 Configuração Personalizada
Crie application.properties no backend para configurações customizadas:

properties
spring.datasource.url=jdbc:postgresql://localhost:5432/vmcontrol_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
server.port=8080
🐛 Solução de Problemas
Banco de Dados
bash
# Verificar se PostgreSQL está rodando
sudo service postgresql status

# Iniciar PostgreSQL se necessário
sudo service postgresql start
Dependências Frontend
bash
# Se houver problemas no npm install
``` 
rm -rf node_modules package-lock.json
npm cache clean --force
npm install
``` 
Portas em Uso
Backend: 8080

Frontend: 65534

PostgreSQL: 5432

📁 Estrutura do Projeto
``` 
Backend/vmcontrol/
├── src/main/java/
│   └── com/vmcontrol/
│       ├── controller/    # Endpoints REST
│       ├── service/       # Regras de negócio
│       ├── repository/    # Acesso a dados
│       ├── model/         # Entidades JPA
│       └── config/        # Configurações
├── build.gradle           # Dependências Gradle
├── gradlew                # Gradle Wrapper
└── settings.gradle        # Configurações do projeto
```
``` 
Frontend/vmcontrol/
├── src/app/
│   ├── components/        # Componentes Angular
│   ├── services/          # Comunicação com API
│   ├── models/            # Interfaces TypeScript
│   └── app.module.ts      # Módulo principal
├── package.json          # Dependências npm
└── angular.json          # Configuração Angular
``` 
🛠️ Tecnologias Utilizadas
Backend
Spring Boot 17

Spring Data JPA

PostgreSQL

Swagger/OpenAPI

Frontend
Angular 17

TypeScript

HTML5/CSS3

🔗 Acesso à Aplicação
Frontend:```  http://localhost:65534``` 

Backend API:```  http://localhost:8080/api``` 

Swagger: ``` http://localhost:8080/api/swagger-ui/index.html#``` 

Banco de Dados: PostgreSQL em ``` localhost:5432/vmcontrol_db``` 
