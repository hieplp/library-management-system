package dev.hieplp.library.common.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil {
    public static byte[] decodeByteFile(String fileName) throws IOException {
        File f = getFileFromResource(fileName);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] bytes = new byte[fis.available()];
        dis.readFully(bytes);
        dis.close();
        return bytes;
    }

    public static File getFileFromResource(String fileName) throws IOException {
        Resource resource = new ClassPathResource(fileName);
        return resource.getFile();
    }
}
