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

#### Criar carteira
```
POST /wallets
{
  "ownerName": String
}
```

#### Ver limite disponível
```
GET /wallets/{walletId}/limits
```

#### Realizar pagamento
```
POST /wallets/{walletId}/payments

{
  "amount": Decimal,
  "Date": Date
}
```

## Observações
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
- Todos os exemplos de endpoints são opcionais, você possui total liberdade para realizar as operações necessárias da forma que quiser, contanto que as regras sejam respeitadas

## Pré-Requisitos

Além da API, o desafio só será aceito para avaliação se contiver os seguintes requisitos:
- Documentação dos endpoints (uma collection no postman seria ótimo)
- Testes
- Instruções para subir a aplicação e suas dependencias para testes manuais

## Critérios de Avaliação

Além dos requisitos levantados acima, iremos olhar para os seguintes critérios durante a correção do desafio:

- Arquitetura (DDD, Clean Architecture)
- Documentação (comente sobre decisões técnicas, escolhas, requisitos, etc)
- Código limpo (utilização de princípios como DRY, KISS, SOLID, YAGNI)
- Testes (unitários, e2e, etc)
- Padrão de commits (Conventional)

### Seria legal

- Utilização de docker

## Submissão
Para submeter o desafio, envie para o e-mail da trace (dev.accounts@trace.finance) a URL do repositório no github e de permissão de leitura para o usuário @tracefinancedev
