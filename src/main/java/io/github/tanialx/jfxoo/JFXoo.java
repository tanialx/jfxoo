package io.github.tanialx.jfxoo;

import java.lang.reflect.InvocationTargetException;

public class JFXoo {

    public static final String JFXOO_CREATOR = "io.github.tanialx.jfxoo.JFXooCreatorImpl";
    private static JFXoo _instance;
    private final JFXooCreator creator;

    private JFXoo() throws ClassNotFoundException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        Class<?> _class = JFXoo.class.getClassLoader().loadClass(JFXOO_CREATOR);
        creator = (JFXooCreator) _class.getConstructors()[0].newInstance();
    }

    public static JFXoo init() throws ClassNotFoundException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        if (_instance == null) {
            _instance = new JFXoo();
        }
        return _instance;
    }

    public JFXooForm get(String name) {
        return creator.create(name);
    }
}
