package org.kaikikm.threadresloader;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

public final class ThreadResLoader {

    private ThreadResLoader() {
    }

    private static final InheritableThreadLocal<AccessibleURLClassLoader> classLoader = new InheritableThreadLocal<AccessibleURLClassLoader>() {
        @Override
        protected AccessibleURLClassLoader initialValue() {
            return new AccessibleURLClassLoader(new URL[0], Thread.currentThread().getContextClassLoader());
        }
        
        @Override
        protected AccessibleURLClassLoader childValue(AccessibleURLClassLoader parentValue) {
            return new AccessibleURLClassLoader(new URL[0], parentValue);
        }
    };
    
    public static void inizializeWithCurrentThreadSettings() {
        ThreadResLoader.classLoader.set(new AccessibleURLClassLoader(new URL[0], Thread.currentThread().getContextClassLoader()));
    }
    
    public static void inizializeWithCurrentThreadSettings(URL... urls) {
        ThreadResLoader.classLoader.set(new AccessibleURLClassLoader(urls, Thread.currentThread().getContextClassLoader()));
    }

    public static void inizializeWithCurrentThreadSettings(Collection<URL> urls) {
        inizializeWithCurrentThreadSettings(urls.toArray(new URL[urls.size()]));
    }
    
    public static void inizializeWithParentThreadSettings(URL... urls) {
        ThreadResLoader.classLoader.set(new AccessibleURLClassLoader(urls, classLoader.get()));
    }

    public static void inizializeWithParentThreadSettings(Collection<URL> urls) {
        inizializeWithParentThreadSettings(urls.toArray(new URL[urls.size()]));
    }

    public static URL getResource(String path) {
        return classLoader.get().getResource(path);
    }

    public static InputStream getResourceAsStream(String path) {
        return classLoader.get().getResourceAsStream(path);
    }
    
    public static void addURL(URL url) {
        ThreadResLoader.classLoader.get().addURL(url);
    }
}
