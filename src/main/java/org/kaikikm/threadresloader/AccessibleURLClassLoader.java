package org.kaikikm.threadresloader;

import java.net.URL;
import java.net.URLClassLoader;

class AccessibleURLClassLoader extends URLClassLoader {
    
    private static final URL[] EMPTY = new URL[0];

    public AccessibleURLClassLoader(ClassLoader parent, URL... urls) {
        super(urls, parent);
    }
    
    public AccessibleURLClassLoader(ClassLoader parent) {
        this(parent, EMPTY);
    }
    
    public AccessibleURLClassLoader(URL... urls) {
        this(Thread.currentThread().getContextClassLoader(), urls);
    }
    
    public AccessibleURLClassLoader() {
        this(EMPTY);
    }
    
    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }
    
}
