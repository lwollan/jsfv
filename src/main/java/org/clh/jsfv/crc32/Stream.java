package org.clh.jsfv.crc32;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

public class Stream {

    private static long calculateCRC32(InputStream fis) throws IOException {
        CRC32 crc = new CRC32();
        int len = 0;
        int off = 0;
        int bufferSize = fis.available() > 0 ? fis.available() : 512;
        byte[] fileBuffer = new byte[bufferSize];
        while (len != -1) {
            len = fis.read(fileBuffer);
            if (len == -1) {
                fis.close();
                break;
            } else {
                crc.update(fileBuffer, off, len);
            }
        }
        long calculatedChecksum = crc.getValue();
        return calculatedChecksum;
    }

    public static long calculateCRC32(File file) throws IOException {
        InputStream fis = new FileInputStream(file);
        long crc32 = calculateCRC32(fis);
        fis.close();
        return crc32;
    }
}
