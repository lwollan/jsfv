package org.clh.jsfv.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TestParsingOfSfvFile {

    @Test
    public void testThatFilesInSFVFileIsRead() throws IOException {
        File sourceSfvFile = new File("valid.sfv");
        File parentDir = sourceSfvFile.getParentFile();
        InputStream source = new FileInputStream(sourceSfvFile);
        ReadSFVFile reader = new ReadSFVFile(source);
        String[] files = reader.getFilesInSFV();
        for (String file : files) {
            SfvChecksum sfv = reader.getSfvForFile(file);
            processFile(parentDir, file, sfv);
        }
    }

    private void processFile(File parentDir, String file, SfvChecksum expected)
            throws FileNotFoundException, IOException {
        File sourceFile = new File(parentDir, file);
        if (sourceFile.exists() && sourceFile.isFile()) {
            Long calculatedChecksum = processfile(sourceFile);
            processResult(expected, calculatedChecksum, sourceFile);
        } else {
            createMissingFile(sourceFile);
        }
    }

    private void createMissingFile(File file) throws IOException {
        File missingFile = new File(file.getAbsoluteFile() + "-MISSING");
        missingFile.createNewFile();
        System.out.println(file + " file missing.");
    }

    private Long processfile(File sourceFile)
            throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(sourceFile);
        return processfile(fis);
    }

    private Long processfile(InputStream fis) throws IOException {
        CRC32 crc = new CRC32();
        int len = 0;
        int off = 0;
        byte[] fileBuffer = new byte[512];
        while (len != -1) {
            len = fis.read(fileBuffer);
            if (len == -1) {
                fis.close();
                break;
            } else {
                crc.update(fileBuffer, off, len);
            }
        }
        return crc.getValue();
    }

    private void processResult(SfvChecksum sfv, long value, File sourceFile) {
        if (value == sfv.getCrc32() ) {
            // see if there is a missing or failed file
            System.out.println(sourceFile + " file ok.");
        } else {
            sourceFile.renameTo(new File(sourceFile.getAbsoluteFile() + "-FAILED"));
            System.out.println(sourceFile + " file failed.");
        }
    }

}
