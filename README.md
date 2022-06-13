# 🚀 Back end challenge - PaymentAPI

Desafio para avaliar o candidato que queira ingressar na Trace Finance como desenvolvedor back-end

## Contexto

Precisamos criar uma API que realiza pagamentos para uma carteira especifica.

Antes do pagamento ser autorizado é necessário validar se a carteira que está sendo utilizada possui limite para realizar a operação.

## Regras

- Uma carteira deve possuir o nome do dono da mesma e o valor desse atributo não pode ser vazio ou nulo
- O valor máximo para um saque é de 1000 reais
- Durante o periodo diurno, o limite total é de 4000 reais
- Durante o periodo noturno, o limite total é de 1000 reais
- Durante o final de semana, o limite total é de 1000 reais

## Desafio

Criar uma API que realize as seguintes operações:

### Criar carteira

#### Request
```
POST /wallets
{
  "ownerName": String
}
```

#### Response
```
Http status code: 201
{
  "id": UUID
  "ownerName": String
}
```

### Ver limite disponível

#### Request
```
GET /wallets/{walletId}/limits
```

#### Response
```
Http status code: 200
{
  "value": Number
}
```

### Realizar pagamento

#### Request
```
POST /wallets/{walletId}/payments

{
  "amount": Number,
  "Date": String
}
```

#### Response
```
Http status code: 200
```

## Pontos importantes
- O limite é diário e não único
- O limite diurno não interfere no limite noturno, ou seja, uma carteira pode pagar até 5000 reais em um dia de semana (4000 durante o dia, 1000 durante a noite)
- O periodo diurno corresponde ao horário entre as 06:00 até as 18:00, ou seja:
```kotlin
date >= 06:00 && date < 18:00 = diurno
```
- O periodo noturno corresponde ao horário entre as 18:00 até as 06:00, ou seja:
```kotlin
date >= 18:00 && date < 06:00 = noturno
```

## Pré-Requisitos

Além da API, o desafio só será aceito para avaliação se contiver os seguintes requisitos:
- Repositório do desafio privado no Github
- Testes
- Instruções para subir a aplicação e suas dependencias para testes manuais

### Seria legal

- Utilização de docker
- Arquitetura (DDD, Clean Architecture)
- Documentação (comente sobre decisões técnicas, escolhas, requisitos, etc)
- Código limpo (utilização de princípios como DRY, KISS, SOLID, YAGNI)
- Testes (unitários, e2e, etc)
- Padrão de commits (Conventional)

## Observações

- O teste foi desenvolvido para ser feito independente da senioridade, portanto não é necessário implementar tudo que foi pedido na sessão [Seria legal](#seria-legal)
- Foque em fazer muito bem a demanda, ou seja, os requisitos da sessão [Pré-Requisitos](#pr-requisitos)

## Submissão
1. Dar permissão de leitura para o usuário do github @tracefinancedev no repositório do desafio.
2. Enviar um e-mail para bsoares@trace.finance com o assunto: "Vaga Back-end Jr.". No corpo do e-mail, favor enviar a URL do repositório no Github.
