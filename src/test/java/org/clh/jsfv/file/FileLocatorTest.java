package org.clh.jsfv.file;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.matchers.IsCollectionContaining;
import org.mockito.Mockito;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;
import static org.junit.internal.matchers.IsCollectionContaining.hasItem;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileLocatorTest {

    private FileLocator locator;

    @Before
    public void setup() {
        locator = new FileLocator(new SfvFilenameFilter());
    }

    @Test
    public void whenDirectoryIsAFile_thenShouldReturnEmptyList() {
        File baseDirectory = createMockDir("baseDir");
        when(baseDirectory.listFiles(Mockito.<FilenameFilter>any())).thenReturn(null);

        assertThat(locator.listFiles(baseDirectory).size(), is(0));

    }

    @Test
    public void whenADirectoryIsFound_thenFileIsAdded() {
        File baseDirectory = createMockDir("baseDir");
        File subDirectory = createMockDir("subDirectory");
        File sfvFile = createMockFile("dummy.sfv");

        when(baseDirectory.listFiles()).thenReturn(new File[] { subDirectory, sfvFile });

        List<File> files = locator.listFiles(baseDirectory);

        assertThat(files, hasItem(sfvFile));
    }

    @Test
    public void whenADirectoryIsHidden_thenSkipIt() {
        File baseDirectory = createMockDir("baseDir");
        File hiddenSubDirectory = createHiddenMockDir(".AppleDouble");
        File sfvFileInSubDirectory = createMockFile("dummy.sfv");

        when(baseDirectory.listFiles()).thenReturn(new File[] { hiddenSubDirectory });
        when(hiddenSubDirectory.listFiles()).thenReturn(new File[]{sfvFileInSubDirectory});

        List<File> files = locator.listFiles(baseDirectory);

        assertThat(files, not(hasItem(sfvFileInSubDirectory)));

    }

    private static File createMockFile(String fileName) {
        File mockSfvFile = mock(File.class);
        when(mockSfvFile.isDirectory()).thenReturn(Boolean.FALSE);
        when(mockSfvFile.isHidden()).thenReturn(Boolean.FALSE);
        when(mockSfvFile.getName()).thenReturn(fileName);
        return mockSfvFile;
    }

    private static File createHiddenMockDir(String dirName) {
        File mockSfvFile = mock(File.class);
        when(mockSfvFile.isDirectory()).thenReturn(Boolean.TRUE);
        when(mockSfvFile.isHidden()).thenReturn(Boolean.TRUE);
        when(mockSfvFile.getName()).thenReturn(dirName);
        return mockSfvFile;
    }

    private static File createMockDir(String dirName) {
        File mockSfvFile = mock(File.class);
        when(mockSfvFile.isDirectory()).thenReturn(Boolean.TRUE);
        when(mockSfvFile.isHidden()).thenReturn(Boolean.FALSE);
        when(mockSfvFile.getName()).thenReturn(dirName);
        return mockSfvFile;
    }
}
