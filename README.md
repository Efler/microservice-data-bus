<div align="center">
    <h1> Microservice Data Bus </h1>
</div>

Шина данных на базе Apache Kafka, состоящая из отдельных компонентов-микросервисов

---

<div align="center">
    <h2> Scheme </h2>
</div>

>> todo: scheme picture << 

---

<div align="center">
    <h2> Description </h2>
</div>

* Данные поступают во входной топик `Kafka`, обработанные данные считываются из выходного топика `Kafka`
* Данные обрабатываются 3-мя сервисами: фильтрация (`Filtering`), дедубликация (`Deduplication`), обогащение (`Enrichment`)
* Правила для сервисов поступают из соответствующих таблиц базы данных `PostgreSQL`, правила вычитываются раз в заданный интервал _(конфигурация: `updateIntervalSec`)_
* Правила конфигурируются с помощью сервиса-менеджера (`Management`) через `Swagger UI` _(либо же через эндпоинты напрямую)_
* Сервис дедубликации использует `Redis` для хранения состояния дублей
* Сервис обогащения использует `MongoDB` для хранения обогащающих данных
* Все сервисы конфигурируются через переменные среды _(возможна конфигурация через `application.conf` напрямую)_

---

<div align="center">
    <h2> Services </h2>
</div>

## _**Filtering**_

`Description`:
Фильтрует данные по заданным правилам

`Rules`:
* **filter_id**: id фильтра
* **rule_id**: id правила
* **field_name**: json-поле сообщения, по которому выполняется фильтрация
* **filter_function_name**: название функции фильтрации _(equals, contains, not_equals, not_contains)_
* **filter_value**: значение для сравнения

`Configuration`:
* **DB_URL**: JDBC-URL базы данных с правилами фильтрации
* **DB_USER**: имя пользователя базы данных
* **DB_PASSWORD**: пароль базы данных
* **KAFKA_CONSUMER_BOOTSTRAP_SERVERS**: URL для подключения к Kafka
* **KAFKA_CONSUMER_CLIENT_ID**: id клиента-консьюмера Kafka
* **KAFKA_CONSUMER_GROUP_ID**: id группы консьюмеров Kafka
* **KAFKA_CONSUMER_AUTO_OFFSET_RESET**: сдвиг для чтения из топика Kafka
* **KAFKA_CONSUMER_TOPIC**: входной топик Kafka для фильтрации
* **KAFKA_PRODUCER_BOOTSTRAP_SERVERS**: URL для подключения к Kafka
* **KAFKA_PRODUCER_CLIENT_ID**: id клиента-продюсера Kafka
* **KAFKA_PRODUCER_ACKS**: фактор подтверждения отправки от брокеров Kafka
* **KAFKA_PRODUCER_TOPIC**: выходной топик Kafka после фильтрации
* **UPDATE_INTERVAL_SEC**: интервал чтения правил фильтрации из базы данных


## _**Deduplication**_

`Description`:
Очищает данные от дубликатов, используя для хранения ключей дедубликации `Redis` _(несколько правил фильтрации объединяются в один ключ дедубликации)_

`Rules`:
* **deduplication_id**: id сервиса дедубликации
* **rule_id**: id правила
* **field_name**: json-поле сообщения, по которому выполняется дедубликация
* **time_to_live_sec**: время жизни ключа в Redis
* **is_active**: вкл/выкл правила

`Configuration`:
* **DB_URL**: JDBC-URL базы данных с правилами фильтрации
* **DB_USER**: имя пользователя базы данных
* **DB_PASSWORD**: пароль базы данных
* **KAFKA_CONSUMER_BOOTSTRAP_SERVERS**: URL для подключения к Kafka
* **KAFKA_CONSUMER_CLIENT_ID**: id клиента-консьюмера Kafka
* **KAFKA_CONSUMER_GROUP_ID**: id группы консьюмеров Kafka
* **KAFKA_CONSUMER_AUTO_OFFSET_RESET**: сдвиг для чтения из топика Kafka
* **KAFKA_CONSUMER_TOPIC**: входной топик Kafka для фильтрации
* **KAFKA_PRODUCER_BOOTSTRAP_SERVERS**: URL для подключения к Kafka
* **KAFKA_PRODUCER_CLIENT_ID**: id клиента-продюсера Kafka
* **KAFKA_PRODUCER_ACKS**: фактор подтверждения отправки от брокеров Kafka
* **KAFKA_PRODUCER_TOPIC**: выходной топик Kafka после фильтрации
* **REDIS_HOST**: хост для подключения к Redis
* **REDIS_PORT**: порт для подключения к Redis
* **UPDATE_INTERVAL_SEC**: интервал чтения правил фильтрации из базы данных


## _**Enrichment**_

`Description`:
Обогащает данные дополнительной информацией, использует для обогащения документы из `MongoDB`. Несколько правил обогащения объединяются и применяются для одного сообщения. Если два правила обогащают одно и то же поле разными документами, то актуальным правилом является то правило, чей `rule_id` больше. Если одному правилу соответствует несколько документов, то актуальным является тот документ, чей `_id` больше (максимальный из всех). Если по актуальному правилу документа в `MongoDB` нет, то поле обогащается значением по умолчанию из правила. Если правил нет, сообщение не обогащается и отправляется в том виде, в котором есть в выходной топик.

`Rules`:
* **enricher_id**: id обогатителя
* **rule_id**: id правила
* **field_name**: json-поле сообщения, которое нужно обогатить
* **field_name_enrichment**: название поля в коллекции MongoDB для обогащения
* **field_value**: поле сообщения, из которого берется значение поля field_name_enrichment, по которому нужно найти документ в коллекции MongoDB
* **field_default_value**: значение по умолчанию, если значение для обогащения не найдено в MongoDB

`Configuration`:
* **DB_URL**: JDBC-URL базы данных с правилами фильтрации
* **DB_USER**: имя пользователя базы данных
* **DB_PASSWORD**: пароль базы данных
* **KAFKA_CONSUMER_BOOTSTRAP_SERVERS**: URL для подключения к Kafka
* **KAFKA_CONSUMER_CLIENT_ID**: id клиента-консьюмера Kafka
* **KAFKA_CONSUMER_GROUP_ID**: id группы консьюмеров Kafka
* **KAFKA_CONSUMER_AUTO_OFFSET_RESET**: сдвиг для чтения из топика Kafka
* **KAFKA_CONSUMER_TOPIC**: входной топик Kafka для фильтрации
* **KAFKA_PRODUCER_BOOTSTRAP_SERVERS**: URL для подключения к Kafka
* **KAFKA_PRODUCER_CLIENT_ID**: id клиента-продюсера Kafka
* **KAFKA_PRODUCER_ACKS**: фактор подтверждения отправки от брокеров Kafka
* **KAFKA_PRODUCER_TOPIC**: выходной топик Kafka после фильтрации
* **MONGO_CONNECTION_STRING**: строка подключения к MongoDB
* **MONGO_DATABASE**: название базы данных в MongoDB
* **MONGO_COLLECTION**: название коллекции с данными обогащения
* **ENRICHMENT_ID**: id сервиса-обогатителя
* **UPDATE_INTERVAL_SEC**: интервал чтения правил фильтрации из базы данных


## _**Management**_

`Description`:
Позволяет конфигурировать правила фильтрации, дедубликации, обогащения. Валидирует вводимые правила. Имеет несколько метрик.

`Endpoints`:
* **/filter**:
  * **/findAll**: получить информацию о всех фильтрах в БД
  * **/findAll/{id}**: получить информацию о всех фильтрах в БД по filter_id
  * **/find/{filterId}/{ruleId}**: получить информацию о фильтре по filter_id и rule_id
  * **/delete**: удалить информацию о всех фильтрах
  * **/delete/{filterId}/{ruleId}**: удалить информацию по конкретному фильтру filter_id и rule_id
  * **/save**: создать фильтр
* **/deduplication**:
  * **/findAll**: получить информацию о всех правилах дедубликации в БД
  * **/findAll/{id}**: получить информацию о всех правилах дедубликации в БД по deduplication_id
  * **/find/{filterId}/{ruleId}**: получить информацию о правиле дедубликации по deduplication_id и rule_id
  * **/delete**: удалить информацию о всех правилах дедубликации
  * **/delete/{filterId}/{ruleId}**: удалить информацию по конкретному правилу дедубликации с deduplication_id и rule_id
  * **/save**: создать правило дедубликации
* **/enrichment**:
  * **/findAll**: получить информацию о всех правилах обогащения в БД
  * **/findAll/{id}**: получить информацию о всех правилах обогащения в БД по enrichment_id
  * **/find/{filterId}/{ruleId}**: получить информацию о правиле обогащения по enrichment_id и rule_id
  * **/delete**: удалить информацию о всех правилах обогащения
  * **/delete/{filterId}/{ruleId}**: удалить информацию по конкретному правилу обогащения с enrichment_id и rule_id
  * **/save**: создать правило обогащения

`Metrics`:
* **countFilters**: количество правил фильтрации
* **countDeduplications**: количество правил дедубликации
* **countEnrichments**: количество правил обогащения
_(URL по умолчанию: /actuator/metrics)_

`Configuration`:
* **DB_URL**: JDBC-URL базы данных с правилами фильтрации
* **DB_USER**: имя пользователя базы данных
* **DB_PASSWORD**: пароль базы данных
