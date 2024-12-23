## Small REST application for managing a wallet ##

There are only 2 endpoints:

+ **POST api/v1/wallet** - changes the wallet balance
Receives a json object of the following type as input:

```
{
  valletId: UUID,
  operationType: DEPOSIT or WITHDRAW,
  amount: 1000
}
```
+ **GET api/v1/wallets/{WALLET_UUID}** - gets the wallet balance

Stack:
+ java 17
+ Spring 3.2
+ Postgresql
+ Liquibase
