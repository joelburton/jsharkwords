package sharkwords.engines;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.*;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class Registration {

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RegistersEngine {
        String name();
    }

    /**
     * This approach began as a contribution by Paul Kuit at
     * http://stackoverflow.com/questions/1456930/, but his only handled single
     * files in a directory in classpath, not in Jar files. N.B. Does NOT handle
     * system classes!
     *
     * @param packageName string name of package
     * @return list of classes
     * @throws IOException Why
     */
    public static String[] getPackageContent(String packageName)
            throws IOException {

        final String packageAsDirName = packageName.replace(".", "/");
        final List<String> list = new ArrayList<>();
        final Enumeration<URL> urls =
                Thread.currentThread().
                        getContextClassLoader().
                        getResources(packageAsDirName);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            String file = url.getFile();

            switch (url.getProtocol()) {
                case "file" -> {
                    File dir = new File(file);
                    for (File f : Objects.requireNonNull(dir.listFiles())) {
                        list.add(packageAsDirName + "/" + f.getName());
                    }
                }
                case "jar" -> {
                    int colon = file.indexOf(':');
                    int bang = file.indexOf('!');
                    String jarFileName = file.substring(colon + 1, bang);
                    JarFile jarFile = new JarFile(jarFileName);
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry e = entries.nextElement();
                        String jarEntryName = e.getName();
                        if (!jarEntryName.endsWith("/") &&
                                jarEntryName.startsWith(packageAsDirName)) {
                            list.add(jarEntryName);
                        }
                    }
                }
                default -> throw new IllegalStateException(
                        "Dunno what to do with URL " + url);
            }
        }
        return list.toArray(new String[]{});
    }

    /**
     * Find all classes in the given package which have the given
     * class-level annotation class.
     */
    public static List<Class<?>> findAnnotatedClasses(
            String packageName,
            Class<? extends Annotation> annotationClass) {

        List<Class<?>> ret = new ArrayList<>();
        String[] clazzNames;
        try {
            clazzNames = getPackageContent(packageName);
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
        for (String clazzName : clazzNames) {
            if (!clazzName.endsWith(".class")) {
                continue;
            }
            clazzName = clazzName.replace('/', '.').replace(".class", "");
            Class<?> c;
            try {
                c = Class.forName(clazzName);
            } catch (ClassNotFoundException ex) {
                System.err.println("Weird: class " + clazzName +
                        " reported in package but gave CNFE: " + ex);
                continue;
            }

            if (c.isAnnotationPresent(annotationClass) &&
                    !ret.contains(c))
                ret.add(c);

        }
        return ret;
    }

    public static Map<String, Class<? extends AbstractEngine>> getEngines() {
        Map<String, Class<? extends AbstractEngine>> nameToClass = new HashMap<>();

        for (Class<?> cls : findAnnotatedClasses("sharkwords.engines", RegistersEngine.class)) {
            @SuppressWarnings("unchecked")
            Class<? extends AbstractEngine> engineClass = (Class<? extends AbstractEngine>) cls;
            RegistersEngine annotation = engineClass.getAnnotation(RegistersEngine.class);
            nameToClass.put(annotation.name(), engineClass);
        }

        return nameToClass;
    }
}