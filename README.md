# cryptoharvester
 Exchange Rate Generator receaves the real time crypto currency rates and persistes them to the database.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Java SE JDK 1.8,

GRADLE 5.6

### Configuration

The configuration file location /src/main/resources/application.yaml. There are multiple items to configure:

- *flush_periods_s* is a loop timer for persistance procedure. E.g. 1, would make application to save receaved/generated quotes to database
- *synth_flush_period_ms* is a loop timer for synthetic instuments generation procedure. E.g. 500, would make application to generate synthetic(defined) quotes each 500 ms.
- *url* is a MySQL database URL
- *username* is a MySQL database username
- *password* is a MYSQL database password
- *instruments* is an array that contains natural and synthetic quotes. This should contain:
 - *name* the name of the quote, could be of any meaningful form. E.g. BTCUSD
 - *instrument* is an actual currency pair. Should have following foramt "BTC/USD"
 - *depends* **optional** nested array that contains anonimous strings. See Synthetic generation below.

### Installing (Windows)

1. Download and extract repository

2. Configure application

3. Open command line at extracted directory

4. Type:
```
gradle build
```
5. Gradle will generate jar at ../com.crud.json/build/libs/

### Quote object

Quote object contains following fields:

- *id* is an ID of an entity
- *name* is a name of quote loaded from application.yml
- *bid* is a sell price of the pair
- *ask* is a buy price of the pair
- *time* is a time stamp
- *exchange* is a currency pair. E.g. "ETH/USDT"

### Database table ovveride

Application drops and recreates QUOTES table in provided database each time started.

### Synthetic generation

If quote has an optional *depends* list, considering correct formating quote would be generated artificially. The *depends* list should contain only two availible (means natural and declared) quotes. E.g.:

```
      depends:
        - BTC/USDT
        - ETH/USDT
```

Synthetic quote will generates rates in the following way Quote 1/Quote 2. Generation will happen each *synth_flush_period_ms* milliseconds.

### Testing

To run test cases repeat steps 1,2 and 3 then type:
```
gradle test
```
## License

This project is licensed under the [Apache License 2](https://www.apache.org/licenses/LICENSE-2.0)
