package net.thucydides.maven.plugin.utils;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

public class DynamicURLClassLoader extends URLClassLoader{

    private Set<URL> urls;

    public DynamicURLClassLoader(URLClassLoader classLoader) {
        super(classLoader.getURLs());

    }

    @Override
    public void addURL(URL url) {
        if (!getURLsList().contains(url)) {
            getURLsList().add(url);
            super.addURL(url);
        }
    }

    public Set<URL> getURLsList() {
        if(urls == null){
            urls =  new HashSet<URL>();
        }
        return urls;
    }
}
