package com.giraone.pdf;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public final class FileUtil {

    // Hide
    private FileUtil() {
    }

    public static File getFileFromResource(String resourcePath) {

        final URL url = FileUtil.class.getResource(resourcePath);
        if (url == null) {
            return null;
        }
        final File file = new File(url.getFile());
        if (!file.exists()) {
            return null;
        }
        return file;
    }

    public static byte[] readBytesFromResource(String resourcePath) throws IOException {

        final File file = getFileFromResource(resourcePath);
        return file == null ? null : Files.readAllBytes(file.toPath());
    }
}

