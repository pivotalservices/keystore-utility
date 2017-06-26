package io.pivotal.keystore;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class KeyStoreConfigurationTest {

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Before
    public void clearSystemProperties() {
        System.clearProperty(KeyStoreConfiguration.JAVA_KEYSTORE_PASSWORD_PROPERTY);
    }

    @Test
    public void testConfiguration() throws IOException {
        // override JKS destination to PWD
        String dir = Paths.get(".").toAbsolutePath().normalize().toString();
        environmentVariables.set("JKS_ROOT_DIRECTORY", dir);

        environmentVariables.set("KEYSTORE", "ZGFk");
        environmentVariables.set("KEYSTORE_PASSWORD", "password");

        KeyStoreConfiguration.setJKSKeystore("KEYSTORE", "javax.net.ssl.keyStore", "KEYSTORE_PASSWORD", "javax.net.ssl.keyStorePassword");

        // verify that the file was written to disk
        File keystore = new File(dir, "keystore.jks");
        assertTrue(keystore.exists());

        try (Reader reader = new FileReader(keystore)) {
            // verify the decoded contents
            char[] chars = new char[10];
            int count = IOUtils.read(reader, chars);
            assertEquals("dad".length(), count);

            char[] expected = new char[]{'d', 'a', 'd'};
            char[] actual = Arrays.copyOfRange(chars, 0, count);
            Assert.assertArrayEquals(expected, actual);

            // verify that the system properties were set correctly
            assertEquals(Paths.get(keystore.getAbsolutePath()).normalize().toString(), System.getProperty("javax.net.ssl.keyStore"));
            assertEquals("password", System.getProperty("javax.net.ssl.keyStorePassword"));
        } finally {
            //noinspection ResultOfMethodCallIgnored
            keystore.delete();
        }
    }
}
