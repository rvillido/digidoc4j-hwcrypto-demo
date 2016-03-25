# DigiDoc4j hwcrypto demo webapp

This is a Demo webapp for digital signing with the combination of [hwcrypto.js](https://github.com/open-eid/hwcrypto.js/wiki) on the frontend and [DigiDoc4j](https://github.com/open-eid/digidoc4j) on the backend.

 * License: MIT

1. requirements

  * Java 1.8 (might also work with 1.7 - not tested)
  * Apache Maven 3.2 or above

2. Fetch the source

   * git clone https://github.com/rvillido/digidoc4j-hwcrypto-demo.git

3. Build & Run

   * run `mvn clean package && java -jar target/digidoc4j-hwcrypto-demo-1.2-SNAPSHOT.war`
   * This will start an embedded Tomcat server instance at port 8080 over HTTPS (using Spring Boot)
   * You can also just run `mvn clean package` and drop that war to your favorite web server.
   * Open [https://localhost:8080](https://localhost:8080)

## HTTPS Connection

Signing must be done over secure HTTPS connection on the client side. Your Web server must support HTTPS connections.
If you get "not_allowed" error message in the JavaScript console, then the client is using regular HTTP connection.

