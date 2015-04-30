# DSS hwcrypto demo webapp

This is a Demo webapp for digital signig with the combination of [hwcrypto.js](https://github.com/open-eid/hwcrypto.js/wiki) on the frontend and [DSS](https://github.com/esig/dss) on the backend.

 * License: LGPL 2.1
 * &copy; Estonian Information System Authority

1. requirements

  * Java 1.8 (might also work with 1.7 - not tested)
  * Apache Maven 3.2 or above 

2. Fetch the source

   * git clone https://github.com/open-eid/dss-hwcrypto-demo

3. Build & Run

   * mvn clean package
   * deploy to your friendly neighbourhood Java Web container 

## DSS Backend

You can change the DSS url in application.properties file, default is http://dss.nowina.lu/dss-webapp/wservice
