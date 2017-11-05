package org.kaikikm.threadresloader;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

public final class ResourceLoader {

    private ResourceLoader() {
    }

    private static final InheritableThreadLocal<AccessibleURLClassLoader> CLASS_LOADER = new InheritableThreadLocal<AccessibleURLClassLoader>() {
        @Override
        protected AccessibleURLClassLoader initialValue() {
            return new AccessibleURLClassLoader();
        }
        @Override
        protected AccessibleURLClassLoader childValue(AccessibleURLClassLoader parentValue) {
            return new AccessibleURLClassLoader(parentValue);
        }
    };
    
    public static void setDefault() {
        ResourceLoader.CLASS_LOADER.set(new AccessibleURLClassLoader());
    }
    
    public static void setURLs(URL... urls) {
        ResourceLoader.CLASS_LOADER.set(new AccessibleURLClassLoader(urls));
    }

    public static void setURLs(Collection<URL> urls) {
        setURLs(urls.toArray(new URL[urls.size()]));
    }
    
    public static void injectURLs(URL... urls) {
        ResourceLoader.CLASS_LOADER.set(new AccessibleURLClassLoader(CLASS_LOADER.get(), urls));
    }

    public static void injectURLs(Collection<URL> urls) {
        injectURLs(urls.toArray(new URL[urls.size()]));
    }

    public static URL getResource(String path) {
        return CLASS_LOADER.get().getResource(path);
    }

    public static InputStream getResourceAsStream(String path) {
        return CLASS_LOADER.get().getResourceAsStream(path);
    }
    
    public static void addURL(URL url) {
        ResourceLoader.CLASS_LOADER.get().addURL(url);
    }
}
