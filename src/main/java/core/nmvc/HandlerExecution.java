package core.nmvc;

import was.http.HttpRequest;
import was.http.HttpResponse;

import java.lang.reflect.Method;

public class HandlerExecution {
    private Object declaredObject;
    private Method method;

    public HandlerExecution(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    public String handle(HttpRequest request, HttpResponse response) throws Exception {
        return null;
    }
}
