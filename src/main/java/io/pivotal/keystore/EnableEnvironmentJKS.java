package io.pivotal.keystore;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Reads a base64 encoded JKS file from the {@code KEYSTORE} and/or {@code TRUSTSTORE} environment variables.
 * <p>
 * Decodes the contents and writes JKS files to disk.  By default, these files are written to
 * /home/vcap/keystore.jks and /home/vcap/truststore.jks.
 * The directory where these files are written can be overridden by setting the {@code JKS_ROOT_DIRECTORY}
 * environment variable.
 * <p>
 * Passwords for the JKS files can be specified in the {@code KEYSTORE_PASSWORD} and {@code TRUSTSTORE_PASSWORD}
 * environment variables.
 * <p>
 * Sets SSL properties for outbound calls.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(KeyStoreConfiguration.class)
public @interface EnableEnvironmentJKS {
}
