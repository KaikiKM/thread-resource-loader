package org.kaikikm.threadresloader;

import java.net.URL;
import java.net.URLClassLoader;

class AccessibleURLClassLoader extends URLClassLoader {

    private static final URL[] EMPTY = new URL[0];

    AccessibleURLClassLoader(final ClassLoader parent, final URL... urls) {
        super(urls, parent);
    }

    AccessibleURLClassLoader(final ClassLoader parent) {
        this(parent, EMPTY);
    }

    AccessibleURLClassLoader(final URL... urls) {
        this(Thread.currentThread().getContextClassLoader(), urls);
    }

    AccessibleURLClassLoader() {
        this(EMPTY);
    }

    @Override
    public void addURL(final URL url) {
        super.addURL(url);
    }

}
