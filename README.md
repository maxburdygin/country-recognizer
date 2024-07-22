# Project Setup and Running Instructions

## Prerequisites

Before you begin, ensure you have the following installed on your macOS system:

1. **Homebrew**: A package manager for macOS.
2. **Java Development Kit (JDK)**: Required for running Maven and Java applications.
3. **Maven**: A build automation tool for Java projects.
4. **Postgres**
4. **Git**

### Installing Homebrew

If you don't have Homebrew installed, open your terminal and run the following command:

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

### Installing Maven
With Homebrew installed, you can install Maven by running:

```bash
brew install maven
```
Verify the Maven installation:

```bash
mvn -version
```

### Installing Postgres
Open Terminal and execute the following command to install PostgreSQL:
```
brew install postgresql
```

Start PostgreSQL:

```
brew services start postgresql
```

Create a new database and user:

```
createuser -P admin
createdb countrycode -O admin
```


## Cloning the Repository
Clone the repository to your local machine using the following command:

```bash
git clone https://github.com/maxburdygin/country-recognizer.git
```
Navigate to the project directory:

```bash
cd your-repository
```
## Building the Project
To build the project, run the following command in the terminal:

```bash
mvn clean install
```
This command will clean any previous builds and install the project dependencies.

## Running the Application
To run the application, use the following command:

```bash
mvn spring-boot:run
```
This command will start the Spring Boot application.
After you run application make sure postgres is fine: 


```bash
user@192 country-recognizer % psql -h localhost -p 5432 -U admin countrycode
psql (14.12 (Homebrew))
Type "help" for help.

countrycode=> \l
                          List of databases
    Name     | Owner | Encoding | Collate | Ctype | Access privileges 
-------------+-------+----------+---------+-------+-------------------
 countrycode | admin | UTF8     | C       | C     | 
 postgres    | user  | UTF8     | C       | C     | 
 template0   | user  | UTF8     | C       | C     | =c/user          +
             |       |          |         |       | user=CTc/user
 template1   | user  | UTF8     | C       | C     | =c/user          +
             |       |          |         |       | user=CTc/user
(4 rows)
```

After you run application you should be able to see a fresh created table `country_phone_code`

```bash
countrycode=> \c countrycode 
You are now connected to database "countrycode" as user "admin".
countrycode=> \dt
              List of relations
 Schema |        Name        | Type  | Owner 
--------+--------------------+-------+-------
 public | country_phone_code | table | admin
(1 row)

countrycode=> 
```

## Running Tests
To run all the tests in the project, execute the following command:

```bash
mvn test
```

After running mvn test, Maven Surefire Plugin generates detailed reports in the target/surefire-reports directory. You can view these reports for more detailed insights:

```bash
ls target/surefire-reports
```
You will find various files like TEST-*.xml and *.txt, which contain detailed information about test execution and failures.

For even more detailed output, you can run Maven with the -X flag to enable debug logging:

```bash
mvn test -X
```
This will provide extensive debug information, including detailed traces of the test execution process.