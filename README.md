# DSS hwcrypto demo webapp

This is a Demo webapp for digital signig with the combination of [hwcrypto.js](https://github.com/open-eid/hwcrypto.js/wiki) on the frontend and [DSS](https://github.com/esig/dss) on the backend.

 * License: MIT
 * &copy; Estonian Information System Authority

1. requirements

  * Java 1.8 (might also work with 1.7 - not tested)
  * Apache Maven 3.2 or above
  * Java Web server with HTTPS support (e.g. Tomcat)

2. Fetch the source

   * git clone https://github.com/open-eid/dss-hwcrypto-demo

3. Build & Run

   * mvn clean package
   * deploy the war file in the target directory to your friendly neighbourhood Java Web container which has HTTPS enabled

## DSS Backend

You can change the DSS url in application.properties file, default is http://dss.nowina.lu/dss-webapp/wservice

## HTTPS Connection

Signing must be done over secure HTTPS connection on the client side. Your Web server must support HTTPS connections.
If you get "not_allowed" error message in the JavaScript console, then the client is using regular HTTP connection.

## Known issues

For non-Tomcat users: application.properties file refers to 'catalina.base' environment variable to locate Tomcat web server logs directory. If you are deploying it to another web server, then change the location of your log file and remove ${catalina.base}.
