# Instruções para o Projeto Java: EBD Manager

## 1. Visão Geral e Objetivo
- **Descrição:** Projeto responsavel por fazer gerenciamento de classes e alunos de escola biblica dominical.
- **Tipo de projeto:** API REST

## 2. Stack Tecnológica e Versões
- **Linguagem:** Java 25 LTS
- **Gerenciador de Dependências:** Gradle
- **Framework Principal:** Spring Boot 4.x
- **Banco de Dados:** Sqlite
- **Testes:** JUnit 5 + Mockito
- **Documentação da API:** Swagger/OpenAPI
- **Controle de Versão:** Git
- **Bibliotecas Adicionais:** Adicione bibliotecas conforme necessário, mas mantenha o projeto leve e eficiente.

## 3. Arquitetura de Pastas Esperada
Organize o projeto seguindo esta estrutura base de pacotes:

```text
src/
├── main/
│   ├── java/
│   │   └── ebdmanager/
│   │       ├── controller/   # Camada de entrada/endpoints
│   │       ├── service/      # Regras de negócio
│   │       ├── repository/   # Acesso a dados
│   │       ├── model/        # Entidades / Domain objects
│   │       └── dto/          # Objetos de Transferência de Dados
│   resouces/
│       └── application.yml
└── test/
    └── java/
        └── ebdmanager/
```

## 4. Regras e Convenções de Código
- Escreva código limpo, seguindo boas práticas de Orientação a Objetos (SOLID).
- Utilize **Injeção de Dependência** via construtor (evite `@Autowired` em campos).
- Use `Optional` onde houver risco de valores nulos.
- Adicione JavaDocs em métodos públicos e classes principais.
- Trate exceções de forma adequada (crie exceções personalizadas se necessário).

## 5. Entidades do domínio
- **ebd_class**
  - Atributos: id, nome, created_at, updated_at
- **ebd_student**
  - Atributos: id, nome, idade, ebd_class_id (FK), created_at, updated_at
- **enrollment**
  - Atributos: id, ebd_student_id (FK), ebd_class_id (FK), created_at, updated_at
- **attendance**
  - Atributos: id, ebd_class_id (FK), created_at
- **attendance_student**
  - Atributos: id, attendance_id (FK), ebd_student_id (FK), present (boolean), book(boolean), bible(boolean), offering (double), created_at
- **attendance_offering**
  - Atributos: id, attendance_id (FK), total_offering (double), created_at

## 6. Funcionalidades Principais
- Adicione segurança básica (autenticação e autorização) para proteger os endpoints.
- CRUD completo para classes e alunos.
- Registro de presença dos alunos em cada aula.
- Endpoint para marcar presença de alunos em uma aula específica. (attendance e attendance_student)
- Geração de relatórios de frequência e participação por data.
- Geração de relatórios de frequência e participação por aluno (quantas faltas, prensenças, etc... o aluno tem).
- Geração de relatórios de frequência e participação por classe (quantos presentes, ausentes por classe na chamada. dado um dia ou um periodo de dias).
- Geração de relatórios de frequência e participação por período geral (quantos presentes, ausentes por classe na chamada. dado um dia ou um periodo de dias).
- Geração de relatorios gerais (quantos alunos, quantas classes, quantos alunos por classe, etc...).
- Geraçao de relatorios gerais por trimestre (quantos alunos, quantas classes, quantos alunos por classe, quantas presenças, quantas faltas, etc...).
- Api REST para integração com front-end ou outros sistemas.

## 5. Passos de Execução
1. Crie a estrutura inicial de diretórios e o arquivo de configuração do build `build.gradle`).
2. Implemente as entidades do domínio primeiro.
3. Crie a camada de repositório/serviço.
4. Desenvolva os controladores ou a interface de uso.
5. Forneça instruções claras de como compilar e rodar a aplicação via terminal.
