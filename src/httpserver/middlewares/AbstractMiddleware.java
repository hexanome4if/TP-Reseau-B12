package httpserver.middlewares;

import httpserver.Request;
import httpserver.Response;

public abstract class AbstractMiddleware {
    public abstract Response handle(Request request);
}
