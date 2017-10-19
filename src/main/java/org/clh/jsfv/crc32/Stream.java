package org.clh.jsfv.crc32;

import java.io.*;
import java.util.zip.CRC32;

public class Stream {

    private static long calculateCRC32(InputStream source) throws IOException {
        CRC32 crc = new CRC32();
        int bytesRead = 0;
        byte[] inputBuffer = createBuffer(source);
        while (bytesRead != -1) {
            bytesRead = source.read(inputBuffer);
            if (bytesRead == -1) {
                break;
            } else {
                crc.update(inputBuffer, 0, bytesRead);
            }
        }
        return crc.getValue();
    }
    public static long calculateCRC32(File sourceFile) throws IOException {
        try (InputStream source = new FileInputStream(sourceFile)) {
            return calculateCRC32(source);
        }
    }

    private static byte[] createBuffer(InputStream source) throws IOException {
        int bufferSize = source.available() > 0 ? source.available() : 512;
        return new byte[bufferSize];
    }

}
