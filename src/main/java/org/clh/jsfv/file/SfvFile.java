package org.clh.jsfv.file;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

class SfvFile {

    private static Map.Entry<String, Long> getFileNameAndCRC(String jsfvLine) {
        String filePart = jsfvLine.substring(0, jsfvLine.lastIndexOf(" ")).trim();
        String crc16 = jsfvLine.substring(jsfvLine.lastIndexOf(" ")).trim();
        long sfv = Long.parseLong(crc16, 16);
        return new HashMap.SimpleEntry<>(filePart, sfv);
    }

    private static boolean isNotComment(String line) {
        return !line.startsWith(";");
    }

    static Map<String, Long> readSfvFile(File sfvFile) throws IOException {
        Map<String, Long> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(sfvFile)))) {
            reader.lines()
                    .filter(SfvFile::isNotComment)
                    .map(SfvFile::getFileNameAndCRC)
                    .forEach(entry -> map.put(entry.getKey(), entry.getValue()));
        }
        return map;
    }
}
