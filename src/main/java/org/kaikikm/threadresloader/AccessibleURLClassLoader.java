package org.kaikikm.threadresloader;

import java.net.URL;
import java.net.URLClassLoader;

class AccessibleURLClassLoader extends URLClassLoader {

    public AccessibleURLClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
    
    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }
    
}
