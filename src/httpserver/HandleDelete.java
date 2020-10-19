package httpserver;

import java.io.File;

public class HandleDelete extends AbstractHandle {
    @Override
    public Response execute(Request request) {
        Response response = new Response();

        File resource = getResource(request.getPath(), false);

        if (resource == null) {
            response.setStatusCode(404);
            return response;
        }

        if (resource.delete()) {
            response.setStatusCode(204);
        } else {
            response.setStatusCode(500);
        }
        return response;
    }
}
