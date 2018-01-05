package org.clh.jsfv.file;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;

import org.clh.jsfv.logging.logger.SystemOutEventLogger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

@Ignore
public class CheckDirectoryTest {

    @Test
    public void testThatSendingFileIsNotAccepted() throws IOException {
        File file = File.createTempFile("prefix", "postfix");
        try {
            CheckDirectory sfvDirectoryChecker = new CheckDirectory(file);
            Assert.fail("Expected exception.");
        } catch (Exception e) {
        }
    }

    @Test
    public void testThatOneSfvIsAllowedInDirectory() throws IOException {
        File sfvFileMock = Mockito.mock(File.class);
        File dirMock = Mockito.mock(File.class);
        Mockito.when(dirMock.isFile()).thenReturn(Boolean.FALSE);
        File[] sfvFileArrayWithMock = new File[]{sfvFileMock};
        Mockito.when(dirMock.listFiles(Mockito.any(FilenameFilter.class)))
                .thenReturn(sfvFileArrayWithMock);

        try {
            CheckDirectory sfvDirectoryChecker = new CheckDirectory(
                    dirMock);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testThatMultipleSfvIsNotAllowedInDirectory() throws IOException {
        File dirMock = Mockito.mock(File.class);
        Mockito.when(dirMock.isFile()).thenReturn(Boolean.FALSE);
        File[] sfvFileArray = new File[2];
        Mockito.when(dirMock.listFiles(Mockito.any(FilenameFilter.class)))
                .thenReturn(sfvFileArray);

        try {
            new CheckDirectory(dirMock);
            fail();
        } catch (Exception e) {
            Assert.assertNull(e.getMessage());
        }
    }

    @Test
    public void testThatNumberOfMissingFilesIsCountedIsZeroWhenNoFilesAreMissingFromSFV()
            throws IOException {
        File sfvFileMock = Mockito.mock(File.class);
        File dirMock = Mockito.mock(File.class);
        Mockito.when(dirMock.isFile()).thenReturn(Boolean.FALSE);
        File[] sfvFileArrayWithMock = new File[]{sfvFileMock};
        Mockito.when(dirMock.listFiles(Mockito.any(FilenameFilter.class)))
                .thenReturn(sfvFileArrayWithMock);

        CheckDirectory checkDirectory = new CheckDirectory(dirMock);
        checkDirectory.process(new SystemOutEventLogger());
        Assert.assertEquals(0, checkDirectory.getNumberOfMissingFiles(new HashMap<>(), dirMock));
    }

    @Test
    public void testThatNumberOfMissingFilesIsOneWhenOneFileIsMissingFromSFV()
            throws IOException {
        File directory = new File("src/test/resources/onefilemissing");
        CheckDirectory checkDirectory = new CheckDirectory(directory);
        checkDirectory.process(new SystemOutEventLogger());
        Assert.assertEquals(1, checkDirectory.getNumberOfMissingFiles(new HashMap<>(), directory));
    }

    @Test
    public void testThatNumberOfFiledIsOneWhenOneFileIsCorrupt()
            throws IOException {
        File directory = new File("src/test/resources/onecorruptfile");
        CheckDirectory checkDirectory = new CheckDirectory(directory);
        checkDirectory.process(new SystemOutEventLogger());
        Assert.assertEquals(1, checkDirectory.getNumberOfFailedFiles(new HashMap<>(), directory));
    }

    @Test
    public void testThatMissingFilesAreCreated() throws IOException {
        File directory = new File("src/main/resources/nofilesmissing");
        CheckDirectory checkDirectory = new CheckDirectory(directory);
        checkDirectory.process(new SystemOutEventLogger());

    }
}
