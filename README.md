# Keystore Utility

A utility for allowing Spring Boot applications to do TLS in a Cloud Native manner.
 
### Goals:

- application can run locally without TLS
- no explicit configuration is required to run locally
- support for mTLS
- support for self-signed certificates (trust store)

### Approach:

A base64-encoded JKS file is read from the `KEYSTORE` environment variable
and written to a well-known location on disk.  The Java SSL system properties
are set for outbound calls.

The application can then configure the Spring SSL properties to point to the JKS file.
It is recommended that this configuration be specified as environment variables so that
it doesn't kick in when running locally.

### Usage

- add project as a dependency
- annotate application with `@EnableEnvironmentJKS`
- set the `KEYSTORE` environment variable to the base64-encoded contents of the JKS file
- [optionally] set the `KEYSTORE_PASSWORD` environment variable
- [optionally] set the `TRUSTSTORE` and `TRUSTSTORE_PASSWORD` environment variables if a trust store is required
- configure Spring SSL with environment variables:
   - `SERVER_SSL_KEY_STORE=/home/vcap/keystore.jks`
   - `SERVER_SSL_KEY_STORE_PASSWORD` (optional)
   - `SERVER_SSL_TRUST_STORE=/home/vcap/truststore.jks` (optional)
   - `SERVER_SSL_TRUST_STORE_PASSWORD` (optional)
  
**Note:** in order to terminate SSL at the application container, you must deploy the app behind
 Cloud Foundry's [TCP router](https://docs.cloudfoundry.org/adminguide/enabling-tcp-routing.html).