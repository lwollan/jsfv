package org.clh.jsfv.file;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.clh.jsfv.logging.SystemOutEventLogger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

@Ignore
public class SfvCheckDirectoryTest {

    @Test
    public void testThatSendingFileIsNotAccepted() throws IOException {
        File file = File.createTempFile("prefix", "postfix");
        try {
            SfvCheckDirectory sfvDirectoryChecker = new SfvCheckDirectory(file);
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
            SfvCheckDirectory sfvDirectoryChecker = new SfvCheckDirectory(
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
            new SfvCheckDirectory(dirMock);
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

        SfvCheckDirectory sfvCheckDirectory = new SfvCheckDirectory(dirMock);
        sfvCheckDirectory.process(new SystemOutEventLogger());
        Assert.assertEquals(0, sfvCheckDirectory.getNumberOfMissingFiles());
    }

    @Test
    public void testThatNumberOfMissingFilesIsOneWhenOneFileIsMissingFromSFV()
            throws IOException {
        File directory = new File("src/test/resources/onefilemissing");
        SfvCheckDirectory sfvCheckDirectory = new SfvCheckDirectory(directory);
        sfvCheckDirectory.process(new SystemOutEventLogger());
        Assert.assertEquals(1, sfvCheckDirectory.getNumberOfMissingFiles());
    }

    @Test
    public void testThatNumberOfFiledIsOneWhenOneFileIsCorrupt()
            throws IOException {
        File directory = new File("src/test/resources/onecorruptfile");
        SfvCheckDirectory sfvCheckDirectory = new SfvCheckDirectory(directory);
        sfvCheckDirectory.process(new SystemOutEventLogger());
        Assert.assertEquals(1, sfvCheckDirectory.getNumberOfFailedFiles());
    }

    @Test
    public void testThatMissingFilesAreCreated() throws IOException {
        File directory = new File("src/main/resources/nofilesmissing");
        SfvCheckDirectory sfvCheckDirectory = new SfvCheckDirectory(directory);
        sfvCheckDirectory.process(new SystemOutEventLogger());

    }
}
