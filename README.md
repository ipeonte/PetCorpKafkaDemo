# PetAdoption Inc. Demo
Demo project for PetAdoption Inc. corporate operations based on persistence layer (RDBMS), Messaging platform (Kafka)
and multiple API services and adapters.

### Build
mvn clean install -P release

### Run UI test with all services, adapters, embedded HSQLDB database, Embedded Kafka
cd UiTests/PetStoreUiTestShared  
mvn clean install  
cd ../PetStoreUiTest  
mvn test
