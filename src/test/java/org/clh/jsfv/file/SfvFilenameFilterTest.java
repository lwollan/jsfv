package org.clh.jsfv.file;

import org.junit.Before;
import org.junit.Test;

import java.io.FilenameFilter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SfvFilenameFilterTest {

    private FilenameFilter filter;

    @Before
    public void setup() {
        filter = new SfvFilenameFilter();
    }

    @Test
    public void testThatFilesWithSFVsuffixIsAccepted() {
        assertTrue(filter.accept(null, "acceptable.sfv"));
    }

    @Test
    public void testThatFilesWithMixedCasedSFVsuffixIsAccepted() {
        assertTrue(filter.accept(null, "acceptable.sFv"));
    }

    @Test
    public void testThatFilesWithoutSFVsuffixIsNotAccepted() {
        assertFalse(filter.accept(null, "notacceptale.doc"));
    }
}
