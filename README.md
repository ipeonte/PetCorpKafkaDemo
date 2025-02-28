[![Java CI with Maven](https://github.com/ipeonte/PetCorpKafkaDemo/actions/workflows/maven.yml/badge.svg)](https://github.com/ipeonte/PetCorpKafkaDemo/actions/workflows/maven.yml)
# PetCorporation Inc. Demo
Demo project for PetAdoption Inc. corporate operations based on persistence layer (RDBMS), Messaging platform (Kafka)
and multiple API services and adapters.

## Diagrams

![PetCorporateDemo Component Diagram](https://raw.githubusercontent.com/ipeonte/PetCorpKafkaDemo/master/doc/PetCorpComponent.png)

## Build
mvn clean install -P release

## Run UI test with all services, adapters, embedded HSQLDB database, Embedded Kafka
cd UiTests/PetStoreUiTestShared  
mvn clean install  
cd ../PetStoreUiTest  
mvn test
