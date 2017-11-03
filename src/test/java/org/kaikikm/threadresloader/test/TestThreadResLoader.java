package org.kaikikm.threadresloader.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kaikikm.threadresloader.ThreadResLoader;

import com.google.common.io.Files;

public class TestThreadResLoader {
    @Test
    public void testClasspathResourceLoading() {
        assertNotNull(ThreadResLoader.getResource("test.txt"));
        //assertNotNull(ThreadResLoader.getResource("/test.txt"));
        assertNotNull(ThreadResLoader.getResource("testfolder"));
        assertNotNull(ThreadResLoader.getResource("testfolder/test1.txt"));
    }
    
    @Test
    public void testCustomClasspathResourceLoading() throws IOException {
        File dir = Files.createTempDir();
        new File(dir.getAbsolutePath() + File.separator + "test_add.txt").createNewFile();
        ThreadResLoader.inizializeWithCurrentThreadSettings(new URL[]{dir.toURI().toURL()});
        assertNotNull(ThreadResLoader.getResource("test_add.txt"));
        FileUtils.deleteDirectory(dir);
    }
    
    @Test
    public void testParentThreadClasspathResourceLoading() throws IOException, InterruptedException {
        File dir = Files.createTempDir();
        new File(dir.getAbsolutePath() + File.separator + "test_add.txt").createNewFile();
        ThreadResLoader.inizializeWithCurrentThreadSettings(new URL[]{dir.toURI().toURL()});
        assertNotNull(ThreadResLoader.getResource("test_add.txt"));
        TestThread t = new TestThread() {
            @Override
            public void run() {
                super.resource = ThreadResLoader.getResource("test_add.txt");;
            }
        };
        t.start();
        t.join();
        assertNotNull(t.getResource());
        t = new TestThread() {
            @Override
            public void run() {
                ThreadResLoader.inizializeWithCurrentThreadSettings();
                super.resource = ThreadResLoader.getResource("test_add.txt");
            }
        };
        t.start();
        t.join();
        assertNull(t.getResource());
        assertNotNull(ThreadResLoader.getResource("test_add.txt"));
        FileUtils.deleteDirectory(dir);
    }
    
    private class TestThread extends Thread {
        private URL resource;
        
        public TestThread() {
        }
        
        public URL getResource() {
            return this.resource;
        }
    }
}
