package core.nmvc;

import core.annotation.Controller;
import core.exception.CreateInstanceException;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {
    private Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> annotated = this.reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(annotated);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> annotated) {
        Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> clazz : annotated) {
            try {
                controllers.put(clazz, clazz.newInstance());
            } catch (Exception e) {
                throw new CreateInstanceException(e.getMessage());
            }
        }
        return controllers;
    }
}
