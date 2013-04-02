package org.clh.jsfv.file;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FilenameFilter;
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
    public void whenADirectoryIsFound_thenThatDirectoryIsAlsoChecked() {
        FileLocator locator = new FileLocator(new SfvFilenameFilter());
        File baseDirectory = mock(File.class);
        File mockDirectory1 = mock(File.class);
        File mockSfvFile = mock(File.class);

        when(baseDirectory.isDirectory()).thenReturn(Boolean.TRUE);
        when(mockDirectory1.isDirectory()).thenReturn(Boolean.TRUE);
        when(mockSfvFile.isDirectory()).thenReturn(Boolean.FALSE);

        when(baseDirectory.listFiles(Mockito.<FilenameFilter>any())).thenReturn(new File[]{mockDirectory1, mockSfvFile});

        locator.listFiles(baseDirectory);

        verify(mockDirectory1).listFiles(Mockito.<FilenameFilter>any());
    }

    @Test
    public void whenADirectoryIsFound_thenFileIsAdded() {
        FileLocator locator = new FileLocator(new SfvFilenameFilter());
        File baseDirectory = mock(File.class);
        File mockDirectory1 = mock(File.class);
        File mockSfvFile = mock(File.class);

        when(baseDirectory.isDirectory()).thenReturn(Boolean.TRUE);
        when(mockDirectory1.isDirectory()).thenReturn(Boolean.TRUE);
        when(mockSfvFile.isDirectory()).thenReturn(Boolean.FALSE);

        when(baseDirectory.listFiles(Mockito.<FilenameFilter>any())).thenReturn(new File[]{mockDirectory1, mockSfvFile});

        List<File> files = locator.listFiles(baseDirectory);

        assertTrue(files.contains(mockSfvFile));

    }
}
