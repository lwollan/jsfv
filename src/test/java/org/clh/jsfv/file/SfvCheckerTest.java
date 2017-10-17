package org.clh.jsfv.file;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SfvCheckerTest {

    @Test
    public void testThatConsturctorAllowDirectories() {
        try {
            new SfvChecker("src/test/resources");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testThatConsturctorDoesNotAllowFile() {
        try {
            SfvChecker checker = new SfvChecker("src/test/resources/withoutcomments.sfv");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testThatProcessMethodFindAllDirectories() throws IOException {
        SfvChecker checker = new SfvChecker("src/test/resources/");
        checker.process();
    }
}
