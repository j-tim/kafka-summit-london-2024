package com.example.avro.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;

public class ClassPathResourceUtils {

    private static final String DEFAULT_ENCODING = "utf-8";

    private ClassPathResourceUtils() {
    }

    /**
     * Obtains the classpath resource as a {@link String}.
     *
     * @param classPathResourceLocation location of the file on the classpath
     * @return the file content as a UTF encoded String
     * @throws RuntimeException in case of an error
     */
    public static String readClassPathResourceAsString(String classPathResourceLocation) {
        try {
            InputStream inputStream = new ClassPathResource(classPathResourceLocation).getInputStream();
            return new String(FileCopyUtils.copyToByteArray(inputStream), DEFAULT_ENCODING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
