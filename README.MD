## Search Engine

### How to run the application:
* clone the repository
* go to the project directory
* run maven command: `mvn clean install spring-boot:run -pl search-mvc-app`
* open application in [browser](http://localhost:8181/docs/add)

**Please note that you should have installed [Maven](https://maven.apache.org/index.html):exclamation:**

### About the project:
It is a multi-module maven project that consists of 4 modules:
* `document-service` - contains a document related **business logic**
* `document-persistence` - an in-memory implementation of a **document persistence**
* `full-text-searcg-engine` - an in-memory implementation of an **index-based full text search engine**
* `seach-mvc-app` - a **Spring Boot application** that wires up all modules and provides **Thymeleaf based UI**

### Full text search algorithm:
A search is based on indexed data. Indexes are computed when new document is added and are sored in a `HashSet`. 
When a search request comes, an indexes of a search query are computed and then compared to the indexes of existing documents.
**If the document indexes contains all indexes of a search query than document key is returned.**

Indexes are computed using the following simplified algorithm:
1. Split the document into words by removing all punctuation (like `,` and `:`)
2. Convert all words into lowercase
3. Remove all stop words (like `a`, and `the`)

