package core.nmvc;

import com.google.common.collect.Maps;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import org.reflections.ReflectionUtils;
import was.http.HttpRequest;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Handler;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(this.basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Method method : getRequestMappingMethods(controllers.keySet())) {
            this.handlerExecutions.put(createHandlerKey(method.getAnnotation(RequestMapping.class)),
                    new HandlerExecution(controllers.get(method.getDeclaringClass()), method));
        }
    }

    private Set<Method> getRequestMappingMethods(Set<Class<?>> classes) {
        Set<Method> requestMappingMethods = new HashSet<>();
        for (Class<?> clazz : classes) {
            requestMappingMethods.addAll(ReflectionUtils.getAllMethods(clazz,
                    ReflectionUtils.withAnnotation(RequestMapping.class)));
        }
        return requestMappingMethods;
    }

    private HandlerKey createHandlerKey(RequestMapping requestMapping) {
        return new HandlerKey(requestMapping.value(), requestMapping.method());
    }

    public HandlerExecution getHandler(HttpRequest request) {
        String requestUri = request.getPath();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toString());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
