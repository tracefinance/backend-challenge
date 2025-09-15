# 🚀 Back-end Challenge — Payment API (Pleno)

Desafio para avaliar engenheiros(as) back-end na Trace Finance.

## 📋 Sobre este Repositório

Este repositório contém um **scaffold mínimo** usando **Kotlin + Ktor + MongoDB** para facilitar a realização do teste. 

### 🔀 Opções para iniciar:

1. **Usar o scaffold**: Faça um **fork** deste repositório e implemente a solução sobre a base existente
2. **Projeto novo**: Crie um projeto completamente novo do zero (também é permitido)

O scaffold inclui:
- ✅ Configuração básica do Ktor com Jackson
- ✅ Estrutura de rotas (endpoints vazios)
- ✅ Build Gradle configurado

---

## Contexto

Você deve construir uma API de pagamentos para carteiras virtuais (**wallets**) que respeite políticas de limite por período (diurno, noturno, final de semana) e garanta consistência dos registros mesmo em situações de múltiplos acessos simultâneos ou requisições repetidas.

---

## Tecnologias (preferenciais)

- **Linguagem**: Kotlin ou Java (preferencial).  
- **Banco de dados**: **MongoDB** (preferencial).  
- Outras stacks são aceitáveis, desde que atendam aos requisitos funcionais e não funcionais, mas será considerado diferencial seguir as preferências acima.

> Recomendações para MongoDB:
> - Defina índices para consultas de listagem e verificação de regras (ex.: `{ walletId: 1, occurredAt: -1 }` e `{ walletId: 1, createdAt: -1 }`).  
> - Utilize chaves/estratégias que impeçam gravações duplicadas conforme as regras deste desafio (por exemplo, índice único sobre um identificador derivado por carteira + atributos de pagamento, se optar por essa abordagem).

---

## Regras de negócio

- Cada carteira possui `ownerName` (não vazio ou nulo).  
- Valor máximo por pagamento: **R$ 1.000,00**.  
- Limites padrão:
  - **Diurno (06:00–18:00)**: até **R$ 4.000,00** por dia.  
  - **Noturno (18:00–06:00)**: até **R$ 1.000,00** por noite.  
  - **Final de semana** (sábado/domingo, 00:00–24:00): até **R$ 1.000,00** por dia.  
- O limite é **diário por período**. Uma carteira pode chegar a R$ 5.000,00 no mesmo dia útil (4k no período diurno + 1k no noturno).  
- O período deve ser calculado usando o fuso `America/Sao_Paulo`.  
- As bordas devem ser respeitadas:
  - `>= 06:00:00` e `< 18:00:00` → diurno  
  - `>= 18:00:00` e `< 06:00:00` (do dia seguinte) → noturno  
- Cada carteira pode estar vinculada a uma **política de limites**.  
- O sistema deve suportar **mais de um tipo de política**, por **categoria**. Exemplos de categorias:  
  - `VALUE_LIMIT` (limites por valor total/por período) — **categoria da política padrão (default)**.  
  - `TX_COUNT_LIMIT` (limites por quantidade de transações por dia) — exemplo em *Bônus*.  
- A política **ativa** de cada carteira deve ser resolvida em tempo de execução.  
- A inclusão de uma nova política **não deve exigir alterações no fluxo principal de pagamento**.  

> Observação: para este desafio, considere **uma política ativa por carteira**. A política **padrão** deve ser de **categoria `VALUE_LIMIT`** (apenas limites de valor). A tarefa bônus propõe uma política alternativa de **categoria `TX_COUNT_LIMIT`** para demonstrar extensibilidade.

---

## Operações

### Criar carteira
```
POST /wallets
{
  "ownerName": "string"
}
```

**201 Created**
```json
{
  "id": "uuid",
  "ownerName": "string",
  "createdAt": "ISO-8601"
}
```

---

### Consultar políticas da carteira
```
GET /wallets/{walletId}/policies
```

**200 OK**
```json
{
  "data": [
    {
      "id": "uuid",
      "name": "DEFAULT_VALUE_LIMIT",
      "category": "VALUE_LIMIT",
      "maxPerPayment": 1000,
      "daytimeDailyLimit": 4000,
      "nighttimeDailyLimit": 1000,
      "weekendDailyLimit": 1000,
      "createdAt": "2025-09-01T12:00:00-03:00",
      "updatedAt": "2025-09-01T12:00:00-03:00"
    }
  ],
  "meta": {
    "total": 1
  }
}
```

---

### Realizar pagamento
```
POST /wallets/{walletId}/payments

Body:
{
  "amount": 999.99,
  "occurredAt": "2025-09-15T17:59:59-03:00"
}
```

Regras:
- O valor deve ser maior que zero e no máximo R$ 1.000,00.  
- O campo `occurredAt` deve ser informado em formato **ISO-8601**.  
- O sistema deve estar preparado para **não processar o mesmo pagamento mais de uma vez**, mesmo que a requisição seja repetida (por exemplo, devido a falhas de rede ou envios duplicados pelo cliente).   
- Quando duas ou mais requisições acontecerem quase ao mesmo tempo para a mesma carteira, o consumo de limite não pode ultrapassar o valor permitido.  
- Caso julgue necessário, você pode incluir **campos adicionais no corpo ou cabeçalhos da requisição** para permitir a identificação única de tentativas de pagamento.  

**200 OK**
```json
{
  "paymentId": "uuid",
  "status": "APPROVED",
  "amount": 999.99,
  "occurredAt": "2025-09-15T17:59:59-03:00",
}
```

**422** quando não há limite suficiente.  
**409** quando uma tentativa repetida contém dados diferentes de um pagamento já registrado.  
**400** para erros de validação.  

---

### Listar pagamentos (com filtro por data)
```
GET /wallets/{walletId}/payments?startDate=2025-09-01T00:00:00-03:00&endDate=2025-09-15T23:59:59-03:00&cursor=abc123
```

- `startDate` e `endDate` são opcionais. Se ausentes, retornar todos os pagamentos da carteira.  
- Os parâmetros devem estar em formato **ISO-8601**.  
- A listagem deve retornar no formato padrão utilizado em nossas APIs, com `data` e `meta`.  
- A paginação deve ser feita por cursor (`nextCursor`, `previousCursor`).  

**200 OK**
```json
{
  "data": [
    {
      "id": "uuid",
      "walletId": "uuid",
      "amount": 250.00,
      "occurredAt": "2025-09-05T10:30:00-03:00",
      "status": "APPROVED",
      "createdAt": "2025-09-05T10:30:05-03:00",
      "updatedAt": "2025-09-05T10:30:05-03:00"
    },
    {
      "id": "uuid",
      "walletId": "uuid",
      "amount": 500.00,
      "occurredAt": "2025-09-10T20:45:00-03:00",
      "status": "APPROVED",
      "createdAt": "2025-09-10T20:45:10-03:00",
      "updatedAt": "2025-09-10T20:45:10-03:00"
    }
  ],
  "meta": {
    "nextCursor": "def456",
    "previousCursor": null,
    "total": 2,
    "totalMatches": null
  }
}
```

---

### Gerenciar políticas de limite

> **Categoria da política** (campo obrigatório):  
> - `VALUE_LIMIT`: política baseada em valores (limites por período e valor máximo por pagamento).  
> - `TX_COUNT_LIMIT`: política baseada em quantidade de transações por dia (ver *Bônus*).

#### Criar política
```
POST /policies
{
  "name": "DEFAULT_VALUE_LIMIT",
  "category": "VALUE_LIMIT",
  "maxPerPayment": 1000,
  "daytimeDailyLimit": 4000,
  "nighttimeDailyLimit": 1000,
  "weekendDailyLimit": 1000
}
```

#### Listar políticas
```
GET /policies
```

**200 OK**
```json
{
  "data": [
    {
      "id": "uuid",
      "name": "DEFAULT_VALUE_LIMIT",
      "category": "VALUE_LIMIT",
      "maxPerPayment": 1000,
      "daytimeDailyLimit": 4000,
      "nighttimeDailyLimit": 1000,
      "weekendDailyLimit": 1000,
      "createdAt": "2025-09-01T12:00:00-03:00",
      "updatedAt": "2025-09-01T12:00:00-03:00"
    },
    {
      "id": "uuid",
      "name": "Weekday-Plus",
      "category": "VALUE_LIMIT",
      "maxPerPayment": 1000,
      "daytimeDailyLimit": 4000,
      "nighttimeDailyLimit": 2000,
      "weekendDailyLimit": 1000,
      "createdAt": "2025-09-05T15:00:00-03:00",
      "updatedAt": "2025-09-05T15:00:00-03:00"
    }
  ],
  "meta": {
    "nextCursor": null,
    "previousCursor": null,
    "total": 2,
    "totalMatches": null
  }
}
```

#### Associar política a uma carteira
```
PUT /wallets/{walletId}/policy
{
  "policyId": "uuid"
}
```

---

## Pré-requisitos (eliminatórios)

- Repositório privado no GitHub.  
- **Testes automatizados abrangentes** que cubram:  
  
  ### 🧪 Testes Unitários:
  - Validação de regras de negócio (limites por período, valores máximos).
  - Cálculo correto de períodos (diurno/noturno/final de semana).
  - Bordas de horário (05:59:59, 06:00:00, 17:59:59, 18:00:00).
  - Lógica de políticas diferentes (VALUE_LIMIT, TX_COUNT_LIMIT).
  - Tratamento de exceções e validações.
  
  ### 🔗 Testes de Integração:
  - **Persistência**: criação e consulta de carteiras, pagamentos e políticas.
  - **Filtros e paginação**: listagem de pagamentos com filtros de data e cursor.
  - **Reset diário**: verificação de limite zerado em novos períodos.
  - **Políticas dinâmicas**: diferentes categorias de política aplicadas na mesma base de código com estruturas de resposta específicas.
  - **APIs end-to-end**: todos os endpoints retornando estruturas corretas conforme a política ativa.
  
- Instruções claras para rodar a aplicação e dependências.  
- Uso de banco de dados com script ou migração para criar coleções/índices.  
- README técnico com decisões de design e trade-offs.  

---

## Seria legal (diferenciais)

- Estrutura organizada em camadas (DDD, Clean Architecture).  
- OpenAPI/Swagger em `/docs`.  
- Logs estruturados com identificadores de requisição.  
- Métricas simples (quantidade de pagamentos aprovados/recusados).  
- Auditoria dos eventos de pagamento (quem, quando, o quê).  
- Docker Compose com aplicação + **MongoDB**.  
- Workflow de CI (build + test).  
- Commits padronizados (Conventional).  

---

## Bônus (extensibilidade)
 
- Adicione uma categoria de política chamada **`TX_COUNT_LIMIT`** que restrinja o número máximo de transações que uma carteira pode realizar em um mesmo dia (independente do valor).  
- Por exemplo, a política pode limitar a **5 pagamentos por dia**.  
- Ao tentar realizar uma transação além do limite diário permitido, a API deve rejeitar a operação.  
- A solução deve permitir a inclusão de novas políticas semelhantes no futuro de forma simples, sem alterar o fluxo de código existente.  

---

## Submissão

1. Dê permissão de leitura para `@tracefinancedev` no repositório.  
2. Envie e-mail para `backend@trace.finance` com assunto **Vaga Back-end Pleno** e a URL do repo.  
3. No README, explique suas principais decisões de arquitetura e pontos de atenção.  
