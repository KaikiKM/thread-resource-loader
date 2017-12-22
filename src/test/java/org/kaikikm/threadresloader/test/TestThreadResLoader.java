package org.kaikikm.threadresloader.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
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
        Assert.assertNotNull(ResourceLoader.getResource("test.txt"));
        Assert.assertNotNull(ResourceLoader.getResource("testfolder"));
        Assert.assertNotNull(ResourceLoader.getResource("testfolder/test1.txt"));
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
        Assert.assertNotNull(ResourceLoader.getResource("test_add.txt"));
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
        Assert.assertNotNull(ResourceLoader.getResource("test_add.txt"));
        TestThread t = new TestThread() {
            @Override
            public void run() {
                setResource(ResourceLoader.getResource(CREATED_FILE));
            }
        };
        t.start();
        t.join();
        Assert.assertNotNull(t.getResource());
        t = new TestThread() {
            @Override
            public void run() {
                ResourceLoader.setDefault();
                setResource(ResourceLoader.getResource(CREATED_FILE));
            }
        };
        t.start();
        t.join();
        Assert.assertNull(t.getResource());
        Assert.assertNotNull(ResourceLoader.getResource(CREATED_FILE));
        FileUtils.deleteDirectory(dir);
    }

    /**
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws MalformedURLException 
     * 
     */
    @Test
    public void testClassLoading() throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, MalformedURLException {
        try {
            ResourceLoader.classForName("TestClass");
            Assert.fail();
        } catch (ClassNotFoundException e) { //NOPMD
        }
        /*
         * add new folder to classpath
         */
        final String root = new File(ResourceLoader.getResource(".").getPath()).getParent();
        ResourceLoader.addURL(new File(root + File.separator + "externTestResources").toURI().toURL());
        Assert.assertNotNull(ResourceLoader.getResource("dummy.txt"));
        /*
         * test new class in added folder
         */
        Class<?> c = ResourceLoader.classForName("it.kaikikm.test.TestClass2");
        Object o =  c.newInstance();
        Method method = c.getDeclaredMethod("testMethod");
        Assert.assertEquals(2, method.invoke(o));
        /*
         * test old class in new folder (must override old class)
         */
        c = ResourceLoader.classForName("it.kaikikm.test.TestClass");
        o = c.newInstance();
        method = c.getDeclaredMethod("testMethod");
        Assert.assertEquals(3, method.invoke(o));
    }

    private class TestThread extends Thread {
        private URL resource;
        public URL getResource() {
            return this.resource;
        }
        public void setResource(final URL resource) {
            this.resource = resource;
        }
    }
}
