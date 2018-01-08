package org.kaikikm.threadresloader.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kaikikm.threadresloader.ResourceLoader;

import com.google.common.io.Files;

/**
 * 
 *
 */
public class TestThreadResLoader {
    private static final String CREATED_FILE = "test_add.txt";
    /**
     * 
     */
    @Test
    public void testClasspathResourceLoading() {
        assertNotNull(ResourceLoader.getResource("test.txt"));
        //assertNotNull(ThreadResLoader.getResource("/test.txt"));
        assertNotNull(ResourceLoader.getResource("testfolder"));
        assertNotNull(ResourceLoader.getResource("testfolder/test1.txt"));
    }

    /**
     * 
     * @throws IOException 
     */
    @Test
    public void testCustomClasspathResourceLoading() throws IOException {
        final File dir = Files.createTempDir();
        final File testFile = new File(dir.getAbsolutePath() + File.separator + CREATED_FILE);
        assertTrue(testFile.createNewFile());
        ResourceLoader.setURLs(new URL[]{dir.toURI().toURL()});
        assertNotNull(ResourceLoader.getResource("test_add.txt"));
        FileUtils.deleteDirectory(dir);
    }

    /**
     * 
     * @throws IOException 
     * @throws InterruptedException 
     */
    @Test
    public void testParentThreadClasspathResourceLoading() throws IOException, InterruptedException {
        final File dir = Files.createTempDir();
        assertTrue(new File(dir.getAbsolutePath() + File.separator + CREATED_FILE).createNewFile());
        ResourceLoader.setURLs(new URL[]{dir.toURI().toURL()});
        assertNotNull(ResourceLoader.getResource("test_add.txt"));
        TestThread t = new TestThread() {
            @Override
            public void run() {
                setResource(ResourceLoader.getResource(CREATED_FILE));
            }
        };
        t.start();
        t.join();
        assertNotNull(t.getResource());
        t = new TestThread() {
            @Override
            public void run() {
                ResourceLoader.setDefault();
                setResource(ResourceLoader.getResource(CREATED_FILE));
            }
        };
        t.start();
        t.join();
        assertNull(t.getResource());
        assertNotNull(ResourceLoader.getResource(CREATED_FILE));
        FileUtils.deleteDirectory(dir);
    }

    private static class TestThread extends Thread {
        private URL resource;
        public URL getResource() {
            return this.resource;
        }
        public void setResource(final URL resource) {
            this.resource = resource;
        }
    }
}
