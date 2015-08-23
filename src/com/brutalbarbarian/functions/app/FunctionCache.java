package com.brutalbarbarian.functions.app;

import com.brutalbarbarian.functions.exceptions.UninitialisedException;
import com.brutalbarbarian.functions.interfaces.Function;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Lu on 3/8/14.
 */
public class FunctionCache {
    private FunctionCache(String packageName) throws IOException, ClassNotFoundException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        initialise(packageName);
    }

    private List<Function> functions;
    protected void initialise(String packageName) throws IOException, ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        functions = new ArrayList<>();

        for (Class _class : getClasses(packageName)) {
//            boolean implementsFunction = false;
//            for (Class _interface : _class.getInterfaces()) {
//                // Only add if its a function
//                if (_interface.equals(Function.class)) {
//                    implementsFunction = true;
//                    break;
//                }
//            }
            if (Function.class.isAssignableFrom(_class)) {
                Function function = (Function) _class.getConstructor().newInstance();
                functions.add(function);
            }
        }
    }

    private List<Class> getClasses(String packageName) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class> ();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    protected static void initialiseCache(String packageName) throws IOException, ClassNotFoundException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        cache = new FunctionCache(packageName);
    }


    private List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + "." + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    private static FunctionCache cache;
    public static FunctionCache getCache() {
        if (cache == null) {
            throw new UninitialisedException();
        }
        return cache;
    }

    public List<Function> getFunctions() {
        return functions;
    }
}
