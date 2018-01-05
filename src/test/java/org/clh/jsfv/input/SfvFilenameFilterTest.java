package org.clh.jsfv.input;

import org.clh.jsfv.input.SfvFilenameFilter;
import org.junit.Assert;
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
        Assert.assertTrue(filter.accept(null, "acceptable.sfv"));
    }

    @Test
    public void testThatFilesWithMixedCasedSFVsuffixIsAccepted() {
        Assert.assertTrue(filter.accept(null, "acceptable.sFv"));
    }

    @Test
    public void testThatFilesWithoutSFVsuffixIsNotAccepted() {
        Assert.assertFalse(filter.accept(null, "notacceptale.doc"));
    }
}
