package org.clh.jsfv.file;

import java.io.FilenameFilter;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SfvFilenameFilterTest {

    @Test
    public void testThatFilesWithSFVsuffixIsAccepted() {
        FilenameFilter filter = new SfvFilenameFilter();
        Assert.assertTrue(filter.accept(null, "acceptable.sfv"));
    }

    @Test
    public void testThatFilesWithMixedCasedSFVsuffixIsAccepted() {
        FilenameFilter filter = new SfvFilenameFilter();
        Assert.assertTrue(filter.accept(null, "acceptable.sFv"));
    }

    @Test
    public void testThatFilesWithoutSFVsuffixIsNotAccepted() {
        FilenameFilter filter = new SfvFilenameFilter();
        Assert.assertFalse(filter.accept(null, "notacceptale.doc"));
    }
}
