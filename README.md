# EBD Manager - API REST

API REST para gerenciamento de classes, alunos, matrículas, chamadas (frequência e participação) e geração de relatórios estatísticos para Escola Bíblica Dominical (EBD).

---

## 1. Stack Tecnológica
- **Linguagem:** Java 25 LTS
- **Framework:** Spring Boot 4.1.x / Spring 7
- **Gerenciador de Dependências:** Gradle 9.8.x (com Gradle Wrapper incluso)
- **Banco de Dados:** SQLite (com Hibernate Community Dialect)
- **Segurança:** Spring Security (HTTP Basic Authentication, Stateless REST API)
- **Documentação da API:** Swagger UI / OpenAPI 3.0 (`springdoc-openapi`)
- **Testes Automatizados:** JUnit 5 + Mockito + Spring Boot Test / MockMvc
- **Controle de Versão:** Git

---

## 2. Estrutura de Pacotes

```text
src/
├── main/
│   ├── java/
│   │   └── ebdmanager/
│   │       ├── config/        # Configurações de Segurança e OpenAPI/Swagger
│   │       ├── controller/    # Endpoints REST (Classes, Alunos, Matrículas, Chamadas, Relatórios)
│   │       ├── dto/           # Data Transfer Objects com validação Jakarta Bean Validation
│   │       ├── exception/     # Tratamento centralizado de exceções (@RestControllerAdvice)
│   │       ├── model/         # Entidades de domínio JPA (ebd_class, ebd_student, enrollment, etc.)
│   │       ├── repository/    # Repositórios Spring Data JPA
│   │       ├── service/       # Regras de negócio e agregação de relatórios
│   │       └── EbdmanagerApplication.java
│   └── resources/
│       └── application.yml    # Configuração de DataSource SQLite, JPA e Swagger
└── test/
    └── java/
        └── ebdmanager/
            ├── controller/    # Testes dos controladores REST (MockMvc + HTTP Basic)
            └── service/       # Testes unitários com Mockito e JUnit 5
```

---

## 3. Entidades do Domínio

- **`ebd_class`**: Classes da EBD (`id`, `nome`, `created_at`, `updated_at`).
- **`ebd_student`**: Alunos da EBD (`id`, `nome`, `idade`, `ebd_class_id`, `created_at`, `updated_at`).
- **`enrollment`**: Histórico e matrículas dos alunos nas classes (`id`, `ebd_student_id`, `ebd_class_id`, `created_at`, `updated_at`).
- **`attendance`**: Sessão de chamada/aula da turma (`id`, `ebd_class_id`, `created_at`).
- **`attendance_student`**: Presença e itens de participação de cada aluno (`id`, `attendance_id`, `ebd_student_id`, `present`, `book`, `bible`, `offering`, `created_at`).
- **`attendance_offering`**: Totalização da oferta arrecadada na chamada (`id`, `attendance_id`, `total_offering`, `created_at`).

---

## 4. Segurança e Autenticação

A aplicação utiliza autenticação **HTTP Basic** sem sessão de estado (Stateless):

| Usuário | Senha | Perfis (Roles) | Permissões |
|---|---|---|---|
| `admin` | `admin123` | `ROLE_ADMIN`, `ROLE_USER` | Acesso total a todos os endpoints, incluindo exclusões (`DELETE`). |
| `user` | `user123` | `ROLE_USER` | Leitura (`GET`), criação (`POST`) e atualização (`PUT`) de classes, alunos, matrículas, presenças e relatórios. |

> **Nota:** As rotas do Swagger (`/swagger-ui/**`, `/swagger-ui.html` e `/v3/api-docs/**`) são públicas e não exigem autenticação.

---

## 5. Como Compilar e Executar

### Pré-requisitos
- JDK 25 instalado (ou configurado via toolchain Gradle)
- Conexão com a internet para o primeiro download das dependências Gradle

### Compilar e rodar os testes
No terminal, dentro do diretório raiz do projeto:

```bash
# Windows
.\gradlew.bat test

# Linux/macOS
./gradlew test
```

### Gerar o arquivo JAR executável
```bash
# Windows
.\gradlew.bat bootJar

# Linux/macOS
./gradlew bootJar
```
O arquivo gerado estará em `build/libs/ebdmanager-0.0.1-SNAPSHOT.jar`.

### Executar a aplicação
Você pode rodar via Gradle:
```bash
# Windows
.\gradlew.bat bootRun

# Linux/macOS
./gradlew bootRun
```
Ou executar diretamente o arquivo `.jar`:
```bash
java -jar build/libs/ebdmanager-0.0.1-SNAPSHOT.jar
```

A aplicação inicializará na porta padrão **8080** e criará automaticamente o banco de dados SQLite local `ebdmanager.db`.

---

## 6. Documentação Interativa (Swagger / OpenAPI)

Com a aplicação em execução, acesse a documentação interativa e execute requisições via navegador:

👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

No Swagger UI, utilize o botão **Authorize** no canto superior direito e informe:
- **Username:** `admin` (ou `user`)
- **Password:** `admin123` (ou `user123`)

---

## 7. Principais Endpoints da API REST

### Classes (`/api/classes`)
- `POST /api/classes` - Cadastra uma nova turma.
- `GET /api/classes` - Lista todas as turmas e quantidade de alunos.
- `GET /api/classes/{id}` - Busca turma por ID.
- `PUT /api/classes/{id}` - Atualiza o nome da turma.
- `DELETE /api/classes/{id}` - Exclui uma turma (exige `ADMIN`).

### Alunos (`/api/students`)
- `POST /api/students` - Cadastra um aluno e o matricula automaticamente na classe informada.
- `GET /api/students` - Lista alunos (com filtro opcional `?classId=1`).
- `GET /api/students/{id}` - Busca aluno por ID.
- `PUT /api/students/{id}` - Atualiza dados do aluno.
- `DELETE /api/students/{id}` - Exclui um aluno (exige `ADMIN`).

### Matrículas (`/api/enrollments`)
- `POST /api/enrollments` - Matricula ou transfere um aluno para uma classe.
- `GET /api/enrollments/class/{classId}` - Lista alunos matriculados na turma.
- `GET /api/enrollments/student/{studentId}` - Histórico de matrículas do aluno.
- `DELETE /api/enrollments/{id}` - Cancela matrícula (exige `ADMIN`).

### Chamadas e Presenças (`/api/attendances`)
- `POST /api/attendances` - Registra a chamada completa de uma turma em uma aula (presença, revista, bíblia e oferta individual/total).
- `GET /api/attendances/{id}` - Detalhes de uma chamada e itens por aluno.
- `GET /api/attendances/class/{classId}` - Histórico de chamadas da turma.

### Relatórios Analíticos (`/api/reports`)
- `GET /api/reports/by-date?date=YYYY-MM-DD` - Frequência, bíblias, revistas e ofertas do dia com divisão por turmas.
- `GET /api/reports/by-student/{studentId}?startDate=...&endDate=...` - Quantidade de presenças, faltas, percentual de frequência, bíblias, revistas e ofertas do aluno.
- `GET /api/reports/by-class/{classId}?startDate=...&endDate=...` - Total de presentes e ausentes por turma em um dia ou período.
- `GET /api/reports/period?startDate=...&endDate=...` - Resumo geral do período com detalhamento por turma.
- `GET /api/reports/general` - Estatísticas gerais (total de alunos, turmas e média de alunos por turma).
- `GET /api/reports/quarterly?year=2026&quarter=1` - Relatório consolidado por trimestre (Q1, Q2, Q3 ou Q4).

---

## 8. Exemplos de Uso com cURL

### 1. Criar uma Classe
```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/classes \
  -H "Content-Type: application/json" \
  -d '{"nome": "Jovens"}'
```

### 2. Cadastrar um Aluno
```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"nome": "Carlos Silva", "idade": 22, "ebdClassId": 1}'
```

### 3. Lançar a Chamada de uma Turma
```bash
curl -u user:user123 -X POST http://localhost:8080/api/attendances \
  -H "Content-Type: application/json" \
  -d '{
    "ebdClassId": 1,
    "createdAt": "2026-10-18T09:00:00",
    "students": [
      {
        "studentId": 1,
        "present": true,
        "book": true,
        "bible": true,
        "offering": 5.00
      }
    ]
  }'
```

### 4. Consultar Relatório por Aluno
```bash
curl -u user:user123 -X GET "http://localhost:8080/api/reports/by-student/1"
```

### 5. Consultar Relatório Trimestral
```bash
curl -u user:user123 -X GET "http://localhost:8080/api/reports/quarterly?year=2026&quarter=4"
```
