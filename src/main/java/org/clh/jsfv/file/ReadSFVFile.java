package org.clh.jsfv.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class ReadSFVFile {

    private Map<String, SfvChecksum> map = new HashMap<String, SfvChecksum>();

    public ReadSFVFile(InputStream source) throws IOException {
        this(new InputStreamReader(source));
    }

    private ReadSFVFile(Reader reader) throws IOException {
        this(new BufferedReader(reader));
    }

    private ReadSFVFile(BufferedReader bufferedReader) throws IOException {
        parseFileFromReader(bufferedReader);
    }

    private void parseFileFromReader(BufferedReader br) throws IOException {
        String line = "";
        while (line != null) {
            line = readLine(br);
        }
    }

    private String readLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (isValidSFVLine(line)) {
            String fileName = getFilename(line);
            SfvChecksum parsedLong = getSfvValue(line);
            storeFileValuePair(fileName, parsedLong);
        }
        return line;
    }

    private void storeFileValuePair(String fileName, SfvChecksum parsedLong) {
        map.put(fileName, parsedLong);
    }

    private String getFilename(String line) {
        int splitAt = line.lastIndexOf(" ");
        String fileName = line.substring(0, splitAt);
        return fileName;
    }

    private SfvChecksum getSfvValue(String line) {
        int splitAt = line.lastIndexOf(" ");
        String sfv = line.substring(splitAt);
        return new SfvChecksum(sfv);
    }

    private boolean isValidSFVLine(String line) {
        return line != null && !line.startsWith(";");
    }

    public String[] getFilesInSFV() {
        return map.keySet().toArray(new String[0]);
    }

    public SfvChecksum getSfvForFile(String string) {
        return map.get(string);
    }

}
