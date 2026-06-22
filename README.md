# Tiny-Ledger

A simple in-memory ledger API built with Spring Boot. Record deposits and withdrawals, view the current balance, and list transaction history.

## Prerequisites

- Java 21

## Run

From the project root:

```powershell
.\mvnw.cmd spring-boot:run
```

The API starts at `http://localhost:8080`.

## API


| Method | Path            | Description                             |
| ------ | --------------- | --------------------------------------- |
| `POST` | `/transactions` | Record a deposit or withdrawal          |
| `GET`  | `/balance`      | View current balance                    |
| `GET`  | `/transactions` | View transaction history (newest first) |


### POST /transactions

Request:

```json
{
  "type": "DEPOSIT",
  "amount": 100.50
}
```

- `type`: `DEPOSIT` or `WITHDRAWAL`
- `amount`: positive decimal (minimum 0.01)

Response `201 Created`:

```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "type": "DEPOSIT",
  "amount": 100.50,
  "createdAt": "2026-06-22T10:00:00Z"
}
```

### GET /balance

Response `200 OK`:

```json
{
  "balance": 150.50
}
```

### GET /transactions

Response `200 OK`:

```json
[
  {
    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "type": "WITHDRAWAL",
    "amount": 50.00,
    "createdAt": "2026-06-22T11:00:00Z"
  },
  {
    "id": "8b2c4f1a-9d3e-4b5a-8c7d-1e2f3a4b5c6d",
    "type": "DEPOSIT",
    "amount": 100.50,
    "createdAt": "2026-06-22T10:00:00Z"
  }
]
```

### Errors

- `400` — invalid request, validation failure, or insufficient funds on withdrawal

## Examples

Deposit funds:

```powershell
curl -X POST http://localhost:8080/transactions -H "Content-Type: application/json" -d "{\"type\":\"DEPOSIT\",\"amount\":100.50}"
```

Check balance:

```powershell
curl http://localhost:8080/balance
```

Withdraw funds:

```powershell
curl -X POST http://localhost:8080/transactions -H "Content-Type: application/json" -d "{\"type\":\"WITHDRAWAL\",\"amount\":50.00}"
```

View transaction history:

```powershell
curl http://localhost:8080/transactions
```

Attempt withdrawal with insufficient funds (returns `400`):

```powershell
curl -X POST http://localhost:8080/transactions `
  -H "Content-Type: application/json" `
  -d "{\"type\":\"WITHDRAWAL\",\"amount\":1000.00}"
```

## Assumptions

- Single global ledger (no accounts or users)
- Single currency (GBP); amounts are in pounds and pence as decimal values stored as `BigDecimal` (e.g. `100.50` = £100.50)
- Withdrawals are rejected when the balance would go negative
- Data is stored in memory only and is lost when the application restarts
- No authentication or authorisation
- No concurrency guarantees beyond Spring's default request handling
- Transaction history is returned newest first
- Timestamps are in UTC (ISO-8601)

## Tests

```powershell
.\mvnw.cmd test
```

