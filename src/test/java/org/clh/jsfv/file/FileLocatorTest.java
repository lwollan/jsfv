package org.clh.jsfv.file;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileLocatorTest {

    @Test
    public void whenDirectoryIsAFile_thenShouldReturnEmptyList() {
        FileLocator locator = new FileLocator(new SfvFilenameFilter());
        File baseDirectory = mock(File.class);
        when(baseDirectory.listFiles(Mockito.<FilenameFilter>any())).thenReturn(null);

        assertEquals(0, locator.listFiles(baseDirectory).size());

    }

    @Test
    public void whenADirectoryIsFound_thenFileIsAdded() {
        FileLocator locator = new FileLocator(new SfvFilenameFilter());
        File baseDirectory = mock(File.class);
        File mockDirectory1 = mock(File.class);
        File mockSfvFile = mock(File.class);

        when(baseDirectory.listFiles()).thenReturn(new File[] { mockDirectory1, mockSfvFile });

        when(baseDirectory.isDirectory()).thenReturn(Boolean.TRUE);
        when(mockDirectory1.isDirectory()).thenReturn(Boolean.TRUE);
        when(mockSfvFile.isDirectory()).thenReturn(Boolean.FALSE);
        when(mockSfvFile.getName()).thenReturn("dummy.sfv");

        List<File> files = locator.listFiles(baseDirectory);

        assertTrue(files.contains(mockSfvFile));

    }
}
