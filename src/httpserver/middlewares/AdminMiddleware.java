package httpserver.middlewares;

import httpserver.AbstractHandle;
import httpserver.Request;
import httpserver.Response;

public class AdminMiddleware extends AbstractHandle {
    @Override
    public Response execute(Request request) {

        if (request.getPath().startsWith("/admin")) {
            String auth = request.getHeader("Authorization");
            Response response = new Response();
            if (auth != null){
                if (auth.equals("Basic secret")) {
                    return null;
                }
                response.setStatusCode(403);
                return response;
            }
            response.setStatusCode(401);
            return response;
        }

        return null;
    }
}
