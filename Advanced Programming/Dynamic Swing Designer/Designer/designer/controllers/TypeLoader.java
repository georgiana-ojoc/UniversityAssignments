package designer.controllers;

import java.net.URL;
import java.net.URLClassLoader;

public class TypeLoader extends URLClassLoader {
    public TypeLoader() {
        super(new URL[0], ClassLoader.getSystemClassLoader());
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }
}
