package httpserver.middlewares;

import httpserver.AbstractHandle;
import httpserver.Request;
import httpserver.Response;

/**
 *
 */
public class AdminMiddleware extends AbstractHandle {
    /**
     * Check if the resource is an admin resource and should be protected
     * (ie. the request should have an authorization header with a basic authentication token)
     * @param request the http request
     * @return an error response if the authentication cannot be granted or null if everything is good
     */
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
