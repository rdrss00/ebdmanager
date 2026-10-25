# Documentação de Endpoints da API - EBD Manager

Esta documentação descreve todos os endpoints REST disponibilizados pelo sistema **EBD Manager**, incluindo finalidade, permissões de acesso, parâmetros de requisição, formato de corpo (*payload*) esperado e exemplos completos de respostas (*response*).

---

## 1. Informações Gerais

- **Base URL:** `http://localhost:8080`
- **Formato de Dados:** `application/json` (em payloads e responses)
- **Codificação:** `UTF-8`
- **Autenticação:** HTTP Basic Authentication (`Authorization: Basic <credentials>`)
- **Documentação Interativa (Swagger UI):** `http://localhost:8080/swagger-ui.html`
- **OpenAPI Schema (JSON):** `http://localhost:8080/v3/api-docs`

### Credenciais de Acesso Padrão

| Usuário | Senha | Perfil (*Role*) | Permissões |
|---|---|---|---|
| `admin` | `admin123` | `ROLE_ADMIN`, `ROLE_USER` | Acesso completo a todos os endpoints, incluindo exclusões (`DELETE`). |
| `user` | `user123` | `ROLE_USER` | Consulta (`GET`), criação (`POST`) e atualização (`PUT`) em todos os recursos. |

> **Nota:** As rotas do Swagger (`/swagger-ui/**`, `/swagger-ui.html`, `/v3/api-docs/**`) são públicas. Todos os métodos `DELETE` no caminho `/api/**` exigem o perfil `ROLE_ADMIN`.

---

## 2. Índice de Endpoints

| Método | Endpoint | Finalidade | Perfil Mínimo |
|---|---|---|---|
| **Classes** | | | |
| `POST` | `/api/classes` | Criar nova classe/turma | `USER` |
| `GET` | `/api/classes` | Listar todas as classes | `USER` |
| `GET` | `/api/classes/{id}` | Buscar classe por ID | `USER` |
| `PUT` | `/api/classes/{id}` | Atualizar dados da classe | `USER` |
| `DELETE` | `/api/classes/{id}` | Excluir classe | `ADMIN` |
| **Alunos** | | | |
| `POST` | `/api/students` | Cadastrar aluno (com matrícula automática) | `USER` |
| `GET` | `/api/students` | Listar alunos (filtro opcional por turma) | `USER` |
| `GET` | `/api/students/{id}` | Buscar aluno por ID | `USER` |
| `PUT` | `/api/students/{id}` | Atualizar cadastro/turma do aluno | `USER` |
| `DELETE` | `/api/students/{id}` | Excluir aluno | `ADMIN` |
| **Matrículas** | | | |
| `POST` | `/api/enrollments` | Matricular ou transferir aluno em turma | `USER` |
| `GET` | `/api/enrollments/class/{classId}` | Listar matrículas de uma classe | `USER` |
| `GET` | `/api/enrollments/student/{studentId}` | Histórico de matrículas do aluno | `USER` |
| `DELETE` | `/api/enrollments/{id}` | Cancelar matrícula | `ADMIN` |
| **Chamadas e Presenças** | | | |
| `POST` | `/api/attendances` | Registrar chamada de turma e presenças | `USER` |
| `GET` | `/api/attendances/{id}` | Buscar detalhes de uma chamada por ID | `USER` |
| `GET` | `/api/attendances/class/{classId}` | Listar histórico de chamadas da turma | `USER` |
| **Relatórios** | | | |
| `GET` | `/api/reports/by-date` | Relatório consolidado por data | `USER` |
| `GET` | `/api/reports/by-student/{studentId}` | Relatório analítico individual do aluno | `USER` |
| `GET` | `/api/reports/by-class/{classId}` | Relatório consolidado de uma classe | `USER` |
| `GET` | `/api/reports/period` | Relatório geral em período customizado | `USER` |
| `GET` | `/api/reports/general` | Resumo quantitativo global da EBD | `USER` |
| `GET` | `/api/reports/quarterly` | Relatório estatístico por trimestre | `USER` |

---

## 3. Padrão de Respostas de Erro

Quando ocorre uma falha na requisição (erro de validação, regra de negócio ou recurso inexistente), a API retorna um objeto JSON padronizado com o seguinte formato:

```json
{
  "timestamp": "2026-10-24T21:15:30.123456",
  "status": 400,
  "error": "Bad Request",
  "message": "Já existe uma classe cadastrada com o nome: Jovens",
  "path": "/api/classes",
  "fieldErrors": null
}
```

Em casos de **validação de campos** (anotações Jakarta Bean Validation), o campo `fieldErrors` traz um mapa contendo os campos inválidos e seus respectivos motivos:

```json
{
  "timestamp": "2026-10-24T21:15:30.123456",
  "status": 400,
  "error": "Validation Error",
  "message": "Erro de validação nos campos informados",
  "path": "/api/classes",
  "fieldErrors": {
    "nome": "O nome da classe é obrigatório"
  }
}
```

### Códigos de Status HTTP Frequentes
- `200 OK`: Operação de consulta ou atualização realizada com sucesso.
- `201 Created`: Recurso cadastrado com sucesso.
- `204 No Content`: Exclusão executada com sucesso (sem conteúdo no corpo).
- `400 Bad Request`: Parâmetros inválidos, campos obrigatórios ausentes ou violação de regra de negócio.
- `401 Unauthorized`: Autenticação HTTP Basic ausente ou inválida.
- `403 Forbidden`: Usuário autenticado não possui permissão para o recurso (ex: perfil `user` tentando executar `DELETE`).
- `404 Not Found`: Recurso não localizado para o identificador informado.
- `500 Internal Server Error`: Erro inesperado do servidor.

---

## 4. Endpoints de Classes (`/api/classes`)

Endpoints para administração e controle das turmas/classes da EBD.

### 4.1. Criar Nova Classe
- **Método:** `POST`
- **Rota:** `/api/classes`
- **Descrição:** Cadastra uma nova turma no sistema. O nome deve ser único (insensível a maiúsculas/minúsculas).
- **Headers:** `Content-Type: application/json`
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Request Payload
```json
{
  "nome": "Jovens"
}
```
| Campo | Tipo | Obrigatório | Descrição / Regra |
|---|---|---|---|
| `nome` | String | Sim | Nome da classe. Tamanho entre 2 e 100 caracteres. Não pode estar em branco. |

#### Response
- **Status:** `201 Created`
- **Body:**
```json
{
  "id": 1,
  "nome": "Jovens",
  "createdAt": "2026-10-24T20:30:00",
  "updatedAt": "2026-10-24T20:30:00",
  "totalStudents": 0
}
```

---

### 4.2. Listar Todas as Classes
- **Método:** `GET`
- **Rota:** `/api/classes`
- **Descrição:** Retorna a lista com todas as classes cadastradas, incluindo a contagem atualizada de alunos em cada uma.
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Request Payload
Nenhum.

#### Response
- **Status:** `200 OK`
- **Body:**
```json
[
  {
    "id": 1,
    "nome": "Jovens",
    "createdAt": "2026-10-24T20:30:00",
    "updatedAt": "2026-10-24T20:30:00",
    "totalStudents": 15
  },
  {
    "id": 2,
    "nome": "Adolescentes",
    "createdAt": "2026-10-24T20:32:00",
    "updatedAt": "2026-10-24T20:32:00",
    "totalStudents": 10
  }
]
```

---

### 4.3. Buscar Classe por ID
- **Método:** `GET`
- **Rota:** `/api/classes/{id}`
- **Descrição:** Recupera as informações detalhadas de uma classe específica.
- **Parâmetros de Rota (Path Parameters):**
  - `id` (Long, Obrigatório): Identificador único da classe.
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Request Payload
Nenhum.

#### Response
- **Status:** `200 OK`
- **Body:**
```json
{
  "id": 1,
  "nome": "Jovens",
  "createdAt": "2026-10-24T20:30:00",
  "updatedAt": "2026-10-24T20:30:00",
  "totalStudents": 15
}
```

---

### 4.4. Atualizar Classe
- **Método:** `PUT`
- **Rota:** `/api/classes/{id}`
- **Descrição:** Atualiza o nome de uma turma existente. O novo nome não pode colidir com o nome de outra classe cadastrada.
- **Parâmetros de Rota:**
  - `id` (Long, Obrigatório): Identificador da classe.
- **Headers:** `Content-Type: application/json`
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Request Payload
```json
{
  "nome": "Jovens e Líderes"
}
```

#### Response
- **Status:** `200 OK`
- **Body:**
```json
{
  "id": 1,
  "nome": "Jovens e Líderes",
  "createdAt": "2026-10-24T20:30:00",
  "updatedAt": "2026-10-24T20:45:00",
  "totalStudents": 15
}
```

---

### 4.5. Excluir Classe
- **Método:** `DELETE`
- **Rota:** `/api/classes/{id}`
- **Descrição:** Remove permanentemente uma classe do sistema.
- **Parâmetros de Rota:**
  - `id` (Long, Obrigatório): Identificador da classe a ser removida.
- **Permissão:** Exige `ROLE_ADMIN`

#### Response
- **Status:** `204 No Content` (sem conteúdo)

---

## 5. Endpoints de Alunos (`/api/students`)

Endpoints para cadastro, consulta e manutenção de alunos.

### 5.1. Cadastrar Novo Aluno
- **Método:** `POST`
- **Rota:** `/api/students`
- **Descrição:** Cadastra um novo aluno e cria automaticamente o primeiro registro de matrícula na classe informada.
- **Headers:** `Content-Type: application/json`
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Request Payload
```json
{
  "nome": "Carlos Eduardo Silva",
  "idade": 21,
  "ebdClassId": 1
}
```
| Campo | Tipo | Obrigatório | Descrição / Regra |
|---|---|---|---|
| `nome` | String | Sim | Nome completo do aluno (entre 2 e 150 caracteres). |
| `idade` | Integer | Sim | Idade do aluno (não pode ser valor negativo). |
| `ebdClassId` | Long | Sim | Identificador da classe à qual o aluno pertence inicialmente. |

#### Response
- **Status:** `201 Created`
- **Body:**
```json
{
  "id": 1,
  "nome": "Carlos Eduardo Silva",
  "idade": 21,
  "ebdClassId": 1,
  "ebdClassName": "Jovens",
  "createdAt": "2026-10-24T20:35:00",
  "updatedAt": "2026-10-24T20:35:00"
}
```

---

### 5.2. Listar Alunos
- **Método:** `GET`
- **Rota:** `/api/students`
- **Descrição:** Lista os alunos cadastrados. Permite filtragem opcional pelo ID da classe.
- **Parâmetros de Consulta (Query Parameters):**
  - `classId` (Long, Opcional): Filtrar apenas alunos vinculados à classe informada.
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Exemplos de Requisição
- Todos os alunos: `GET /api/students`
- Alunos da classe 1: `GET /api/students?classId=1`

#### Response
- **Status:** `200 OK`
- **Body:**
```json
[
  {
    "id": 1,
    "nome": "Carlos Eduardo Silva",
    "idade": 21,
    "ebdClassId": 1,
    "ebdClassName": "Jovens",
    "createdAt": "2026-10-24T20:35:00",
    "updatedAt": "2026-10-24T20:35:00"
  },
  {
    "id": 2,
    "nome": "Mariana Ferreira",
    "idade": 16,
    "ebdClassId": 2,
    "ebdClassName": "Adolescentes",
    "createdAt": "2026-10-24T20:36:00",
    "updatedAt": "2026-10-24T20:36:00"
  }
]
```

---

### 5.3. Buscar Aluno por ID
- **Método:** `GET`
- **Rota:** `/api/students/{id}`
- **Descrição:** Retorna os dados completos de um aluno específico.
- **Parâmetros de Rota:**
  - `id` (Long, Obrigatório): Identificador único do aluno.
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Response
- **Status:** `200 OK`
- **Body:**
```json
{
  "id": 1,
  "nome": "Carlos Eduardo Silva",
  "idade": 21,
  "ebdClassId": 1,
  "ebdClassName": "Jovens",
  "createdAt": "2026-10-24T20:35:00",
  "updatedAt": "2026-10-24T20:35:00"
}
```

---

### 5.4. Atualizar Aluno
- **Método:** `PUT`
- **Rota:** `/api/students/{id}`
- **Descrição:** Atualiza as informações do aluno (nome, idade ou classe vinculada). Caso `ebdClassId` seja modificado, uma nova matrícula é gerada para a nova turma caso ainda não exista histórico nela.
- **Parâmetros de Rota:**
  - `id` (Long, Obrigatório): Identificador do aluno.
- **Headers:** `Content-Type: application/json`
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Request Payload
```json
{
  "nome": "Carlos Eduardo Silva Santos",
  "idade": 22,
  "ebdClassId": 1
}
```

#### Response
- **Status:** `200 OK`
- **Body:**
```json
{
  "id": 1,
  "nome": "Carlos Eduardo Silva Santos",
  "idade": 22,
  "ebdClassId": 1,
  "ebdClassName": "Jovens",
  "createdAt": "2026-10-24T20:35:00",
  "updatedAt": "2026-10-24T20:50:00"
}
```

---

### 5.5. Excluir Aluno
- **Método:** `DELETE`
- **Rota:** `/api/students/{id}`
- **Descrição:** Exclui um aluno do sistema.
- **Parâmetros de Rota:**
  - `id` (Long, Obrigatório): Identificador do aluno a ser removido.
- **Permissão:** Exige `ROLE_ADMIN`

#### Response
- **Status:** `204 No Content` (sem conteúdo)

---

## 6. Endpoints de Matrículas (`/api/enrollments`)

Endpoints para gestão do vínculo e histórico de matrículas dos alunos nas turmas.

### 6.1. Matricular / Transferir Aluno
- **Método:** `POST`
- **Rota:** `/api/enrollments`
- **Descrição:** Registra a matrícula de um aluno em uma classe. Se o aluno já estiver matriculado na mesma classe informada, retorna erro de regra de negócio (`400 Bad Request`). Atualiza a classe ativa do aluno.
- **Headers:** `Content-Type: application/json`
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Request Payload
```json
{
  "studentId": 1,
  "ebdClassId": 2
}
```
| Campo | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| `studentId` | Long | Sim | Identificador do aluno a matricular. |
| `ebdClassId` | Long | Sim | Identificador da classe destino. |

#### Response
- **Status:** `201 Created`
- **Body:**
```json
{
  "id": 2,
  "studentId": 1,
  "studentName": "Carlos Eduardo Silva",
  "ebdClassId": 2,
  "ebdClassName": "Adolescentes",
  "createdAt": "2026-10-24T20:55:00",
  "updatedAt": "2026-10-24T20:55:00"
}
```

---

### 6.2. Listar Matrículas de uma Classe
- **Método:** `GET`
- **Rota:** `/api/enrollments/class/{classId}`
- **Descrição:** Retorna a lista com todos os alunos matriculados na turma indicada.
- **Parâmetros de Rota:**
  - `classId` (Long, Obrigatório): Identificador da classe.
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Response
- **Status:** `200 OK`
- **Body:**
```json
[
  {
    "id": 1,
    "studentId": 1,
    "studentName": "Carlos Eduardo Silva",
    "ebdClassId": 1,
    "ebdClassName": "Jovens",
    "createdAt": "2026-10-24T20:35:00",
    "updatedAt": "2026-10-24T20:35:00"
  }
]
```

---

### 6.3. Listar Matrículas de um Aluno (Histórico)
- **Método:** `GET`
- **Rota:** `/api/enrollments/student/{studentId}`
- **Descrição:** Retorna o histórico de todas as turmas pelas quais o aluno já foi matriculado.
- **Parâmetros de Rota:**
  - `studentId` (Long, Obrigatório): Identificador do aluno.
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Response
- **Status:** `200 OK`
- **Body:**
```json
[
  {
    "id": 1,
    "studentId": 1,
    "studentName": "Carlos Eduardo Silva",
    "ebdClassId": 1,
    "ebdClassName": "Jovens",
    "createdAt": "2026-10-24T20:35:00",
    "updatedAt": "2026-10-24T20:35:00"
  },
  {
    "id": 2,
    "studentId": 1,
    "studentName": "Carlos Eduardo Silva",
    "ebdClassId": 2,
    "ebdClassName": "Adultos",
    "createdAt": "2026-10-24T21:00:00",
    "updatedAt": "2026-10-24T21:00:00"
  }
]
```

---

### 6.4. Cancelar Matrícula
- **Método:** `DELETE`
- **Rota:** `/api/enrollments/{id}`
- **Descrição:** Remove um registro de matrícula do sistema.
- **Parâmetros de Rota:**
  - `id` (Long, Obrigatório): Identificador do registro de matrícula.
- **Permissão:** Exige `ROLE_ADMIN`

#### Response
- **Status:** `204 No Content` (sem conteúdo)

---

## 7. Endpoints de Chamadas e Presenças (`/api/attendances`)

Endpoints para controle da frequência dominical, revistas, bíblias e arrecadação de ofertas por aula.

### 7.1. Registrar Chamada da Turma
- **Método:** `POST`
- **Rota:** `/api/attendances`
- **Descrição:** Registra a chamada completa de uma turma em uma data informada. Permite lançar para cada aluno: presença, revista/livro, bíblia e oferta individual. O total da oferta pode ser calculado automaticamente pela soma das ofertas individuais ou sobrescrito pelo campo `totalOffering`.
- **Headers:** `Content-Type: application/json`
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Request Payload
```json
{
  "ebdClassId": 1,
  "createdAt": "2026-10-18T09:00:00",
  "totalOffering": 15.00,
  "students": [
    {
      "studentId": 1,
      "present": true,
      "book": true,
      "bible": true,
      "offering": 10.00
    },
    {
      "studentId": 2,
      "present": true,
      "book": false,
      "bible": true,
      "offering": 5.00
    },
    {
      "studentId": 3,
      "present": false,
      "book": false,
      "bible": false,
      "offering": 0.00
    }
  ]
}
```

| Campo | Tipo | Obrigatório | Descrição / Regra |
|---|---|---|---|
| `ebdClassId` | Long | Sim | Identificador da classe da aula. |
| `createdAt` | LocalDateTime | Não | Data e hora da aula (formato ISO-8601: `YYYY-MM-DDTHH:mm:ss`). Se omitido, assume a data/hora atual. |
| `totalOffering` | Double | Não | Total de ofertas da chamada (>= 0). Se nulo, assume a soma dos campos `offering` de cada aluno. |
| `students` | Array | Sim | Lista de registros de alunos da chamada (não pode estar vazia). Não pode haver alunos duplicados. |
| `students[].studentId` | Long | Sim | Identificador do aluno. |
| `students[].present` | Boolean | Não | `true` se presente, `false` se ausente (padrão: `false`). |
| `students[].book` | Boolean | Não | `true` se trouxe revista/livro (padrão: `false`). |
| `students[].bible` | Boolean | Não | `true` se trouxe bíblia (padrão: `false`). |
| `students[].offering` | Double | Não | Valor da oferta individual ofertada pelo aluno (>= 0, padrão: `0.0`). |

#### Response
- **Status:** `201 Created`
- **Body:**
```json
{
  "id": 1,
  "ebdClassId": 1,
  "ebdClassName": "Jovens",
  "createdAt": "2026-10-18T09:00:00",
  "totalOffering": 15.00,
  "totalPresent": 2,
  "totalAbsent": 1,
  "students": [
    {
      "id": 1,
      "studentId": 1,
      "studentName": "Carlos Eduardo Silva",
      "present": true,
      "book": true,
      "bible": true,
      "offering": 10.00,
      "createdAt": "2026-10-18T09:00:00"
    },
    {
      "id": 2,
      "studentId": 2,
      "studentName": "Lucas Martins",
      "present": true,
      "book": false,
      "bible": true,
      "offering": 5.00,
      "createdAt": "2026-10-18T09:00:00"
    },
    {
      "id": 3,
      "studentId": 3,
      "studentName": "Beatriz Lima",
      "present": false,
      "book": false,
      "bible": false,
      "offering": 0.00,
      "createdAt": "2026-10-18T09:00:00"
    }
  ]
}
```

---

### 7.2. Buscar Chamada por ID
- **Método:** `GET`
- **Rota:** `/api/attendances/{id}`
- **Descrição:** Retorna o cabeçalho e a lista com a presença e itens de participação de todos os alunos registrados em uma chamada específica.
- **Parâmetros de Rota:**
  - `id` (Long, Obrigatório): Identificador da chamada.
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Response
- **Status:** `200 OK`
- **Body:** Formato idêntico à resposta de criação (`AttendanceResponseDTO`).

---

### 7.3. Listar Chamadas da Classe
- **Método:** `GET`
- **Rota:** `/api/attendances/class/{classId}`
- **Descrição:** Retorna todo o histórico de sessões de chamada realizadas para a classe informada.
- **Parâmetros de Rota:**
  - `classId` (Long, Obrigatório): Identificador da classe.
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Response
- **Status:** `200 OK`
- **Body:**
```json
[
  {
    "id": 1,
    "ebdClassId": 1,
    "ebdClassName": "Jovens",
    "createdAt": "2026-10-18T09:00:00",
    "totalOffering": 15.00,
    "totalPresent": 2,
    "totalAbsent": 1,
    "students": [ ... ]
  }
]
```

---

## 8. Endpoints de Relatórios Analíticos (`/api/reports`)

Endpoints para suporte à tomada de decisão, extração de indicadores estatísticos de frequência, bíblias, revistas e ofertas.

### 8.1. Relatório por Data
- **Método:** `GET`
- **Rota:** `/api/reports/by-date`
- **Descrição:** Gera relatório analítico de frequência e participação de todas as turmas que tiveram chamada no dia informado, consolidando métricas gerais e divisão por classe.
- **Parâmetros de Consulta (Query Parameters):**
  - `date` (LocalDate, Obrigatório): Data no formato `YYYY-MM-DD` (ex: `2026-10-18`).
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Exemplo de Requisição
```http
GET /api/reports/by-date?date=2026-10-18 HTTP/1.1
Authorization: Basic dXNlcjp1c2VyMTIz
```

#### Response
- **Status:** `200 OK`
- **Body:**
```json
{
  "date": "2026-10-18",
  "totalClassesHeld": 2,
  "totalPresent": 25,
  "totalAbsent": 5,
  "overallAttendanceRate": 83.33,
  "totalBibles": 22,
  "totalBooks": 24,
  "totalOfferings": 120.50,
  "classBreakdowns": [
    {
      "classId": 1,
      "className": "Jovens",
      "totalSessions": 1,
      "totalPresent": 15,
      "totalAbsent": 2,
      "attendanceRate": 88.24,
      "totalBibles": 14,
      "totalBooks": 15,
      "totalOfferings": 70.00
    },
    {
      "classId": 2,
      "className": "Adultos",
      "totalSessions": 1,
      "totalPresent": 10,
      "totalAbsent": 3,
      "attendanceRate": 76.92,
      "totalBibles": 8,
      "totalBooks": 9,
      "totalOfferings": 50.50
    }
  ]
}
```

---

### 8.2. Relatório por Aluno
- **Método:** `GET`
- **Rota:** `/api/reports/by-student/{studentId}`
- **Descrição:** Emite o relatório de frequência e participação individual de um aluno, apresentando contadores de presenças, faltas, índice percentual de frequência, quantidades de bíblias e revistas trazidas, total ofertado e histórico aula a aula.
- **Parâmetros de Rota:**
  - `studentId` (Long, Obrigatório): Identificador do aluno.
- **Parâmetros de Consulta (Query Parameters):**
  - `startDate` (LocalDate, Opcional): Data de início no formato `YYYY-MM-DD` (ex: `2026-01-01`).
  - `endDate` (LocalDate, Opcional): Data de término no formato `YYYY-MM-DD` (ex: `2026-12-31`).
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Exemplo de Requisição
```http
GET /api/reports/by-student/1?startDate=2026-10-01&endDate=2026-10-31 HTTP/1.1
Authorization: Basic dXNlcjp1c2VyMTIz
```

#### Response
- **Status:** `200 OK`
- **Body:**
```json
{
  "studentId": 1,
  "studentName": "Carlos Eduardo Silva",
  "studentAge": 21,
  "className": "Jovens",
  "totalSessions": 4,
  "presentCount": 3,
  "absentCount": 1,
  "attendanceRate": 75.0,
  "bibleCount": 3,
  "bookCount": 3,
  "totalOffering": 30.00,
  "sessions": [
    {
      "attendanceId": 10,
      "date": "2026-10-25T09:00:00",
      "present": true,
      "book": true,
      "bible": true,
      "offering": 10.00
    },
    {
      "attendanceId": 7,
      "date": "2026-10-18T09:00:00",
      "present": true,
      "book": true,
      "bible": true,
      "offering": 10.00
    },
    {
      "attendanceId": 4,
      "date": "2026-10-11T09:00:00",
      "present": false,
      "book": false,
      "bible": false,
      "offering": 0.00
    },
    {
      "attendanceId": 1,
      "date": "2026-10-04T09:00:00",
      "present": true,
      "book": true,
      "bible": true,
      "offering": 10.00
    }
  ]
}
```

---

### 8.3. Relatório por Classe
- **Método:** `GET`
- **Rota:** `/api/reports/by-class/{classId}`
- **Descrição:** Emite relatório consolidado de uma classe específica, calculando o total de alunos matriculados, número de aulas realizadas, soma de presenças, ausências, taxa de frequência percentual, bíblias, revistas, total de ofertas e histórico de cada sessão.
- **Parâmetros de Rota:**
  - `classId` (Long, Obrigatório): Identificador da classe.
- **Parâmetros de Consulta:**
  - `startDate` (LocalDate, Opcional): Data de início (`YYYY-MM-DD`).
  - `endDate` (LocalDate, Opcional): Data de término (`YYYY-MM-DD`).
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Exemplo de Requisição
```http
GET /api/reports/by-class/1?startDate=2026-10-01&endDate=2026-10-31 HTTP/1.1
Authorization: Basic dXNlcjp1c2VyMTIz
```

#### Response
- **Status:** `200 OK`
- **Body:**
```json
{
  "classId": 1,
  "className": "Jovens",
  "totalEnrolledStudents": 20,
  "totalSessions": 4,
  "totalPresent": 72,
  "totalAbsent": 8,
  "attendanceRate": 90.0,
  "totalBibles": 65,
  "totalBooks": 70,
  "totalOfferings": 360.00,
  "sessions": [
    {
      "attendanceId": 10,
      "date": "2026-10-25T09:00:00",
      "presentCount": 19,
      "absentCount": 1,
      "totalOffering": 95.00
    },
    {
      "attendanceId": 7,
      "date": "2026-10-18T09:00:00",
      "presentCount": 18,
      "absentCount": 2,
      "totalOffering": 90.00
    }
  ]
}
```

---

### 8.4. Relatório por Período Geral
- **Método:** `GET`
- **Rota:** `/api/reports/period`
- **Descrição:** Gera relatório analítico consolidado englobando todas as turmas em um intervalo de datas obrigatório, com visão geral e detalhamento classe a classe.
- **Parâmetros de Consulta:**
  - `startDate` (LocalDate, Obrigatório): Data inicial do período (`YYYY-MM-DD`).
  - `endDate` (LocalDate, Obrigatório): Data final do período (`YYYY-MM-DD`).
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Exemplo de Requisição
```http
GET /api/reports/period?startDate=2026-10-01&endDate=2026-10-31 HTTP/1.1
Authorization: Basic dXNlcjp1c2VyMTIz
```

#### Response
- **Status:** `200 OK`
- **Body:**
```json
{
  "startDate": "2026-10-01T00:00:00",
  "endDate": "2026-10-31T23:59:59.999999999",
  "totalSessions": 8,
  "totalPresent": 140,
  "totalAbsent": 20,
  "overallAttendanceRate": 87.5,
  "totalBibles": 128,
  "totalBooks": 135,
  "totalOfferings": 680.00,
  "classBreakdowns": [
    {
      "classId": 1,
      "className": "Jovens",
      "totalSessions": 4,
      "totalPresent": 72,
      "totalAbsent": 8,
      "attendanceRate": 90.0,
      "totalBibles": 65,
      "totalBooks": 70,
      "totalOfferings": 360.00
    },
    {
      "classId": 2,
      "className": "Adultos",
      "totalSessions": 4,
      "totalPresent": 68,
      "totalAbsent": 12,
      "attendanceRate": 85.0,
      "totalBibles": 63,
      "totalBooks": 65,
      "totalOfferings": 320.00
    }
  ]
}
```

---

### 8.5. Relatório Geral Quantitativo
- **Método:** `GET`
- **Rota:** `/api/reports/general`
- **Descrição:** Apresenta panorama cadastral geral da EBD: quantidade total de turmas, quantidade total de alunos matriculados, média de alunos por turma e contagem individual de cada classe.
- **Parâmetros de Consulta:** Nenhum.
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Exemplo de Requisição
```http
GET /api/reports/general HTTP/1.1
Authorization: Basic dXNlcjp1c2VyMTIz
```

#### Response
- **Status:** `200 OK`
- **Body:**
```json
{
  "totalClasses": 3,
  "totalStudents": 45,
  "averageStudentsPerClass": 15.0,
  "classes": [
    {
      "classId": 1,
      "className": "Jovens",
      "studentCount": 20,
      "enrollmentCount": 20
    },
    {
      "classId": 2,
      "className": "Adultos",
      "studentCount": 15,
      "enrollmentCount": 15
    },
    {
      "classId": 3,
      "className": "Adolescentes",
      "studentCount": 10,
      "enrollmentCount": 10
    }
  ]
}
```

---

### 8.6. Relatório Trimestral
- **Método:** `GET`
- **Rota:** `/api/reports/quarterly`
- **Descrição:** Emite relatório analítico completo consolidado para um trimestre específico de um determinado ano (1º Trimestre: Jan-Mar, 2º Trimestre: Abr-Jun, 3º Trimestre: Jul-Set, 4º Trimestre: Out-Dez).
- **Parâmetros de Consulta:**
  - `year` (int, Obrigatório): Ano desejado (ex: `2026`).
  - `quarter` (int, Obrigatório): Número do trimestre (valores válidos: `1`, `2`, `3` ou `4`).
- **Permissão:** `ROLE_USER` ou `ROLE_ADMIN`

#### Exemplo de Requisição
```http
GET /api/reports/quarterly?year=2026&quarter=4 HTTP/1.1
Authorization: Basic dXNlcjp1c2VyMTIz
```

#### Response
- **Status:** `200 OK`
- **Body:**
```json
{
  "year": 2026,
  "quarter": 4,
  "startDate": "2026-10-01",
  "endDate": "2026-12-31",
  "totalClasses": 3,
  "totalStudents": 45,
  "averageStudentsPerClass": 15.0,
  "totalSessions": 12,
  "totalPresent": 216,
  "totalAbsent": 24,
  "attendanceRate": 90.0,
  "totalBibles": 195,
  "totalBooks": 205,
  "totalOfferings": 1080.00,
  "classSummaries": [
    {
      "classId": 1,
      "className": "Jovens",
      "totalSessions": 12,
      "totalPresent": 115,
      "totalAbsent": 11,
      "attendanceRate": 91.27,
      "totalBibles": 100,
      "totalBooks": 105,
      "totalOfferings": 580.00
    },
    {
      "classId": 2,
      "className": "Adultos",
      "totalSessions": 12,
      "totalPresent": 101,
      "totalAbsent": 13,
      "attendanceRate": 88.60,
      "totalBibles": 95,
      "totalBooks": 100,
      "totalOfferings": 500.00
    }
  ]
}
```
