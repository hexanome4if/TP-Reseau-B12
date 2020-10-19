package httpserver;

import java.io.File;
import java.io.IOException;

public class HandlePost extends AbstractHandle {

    @Override
    public Response execute(Request request) {
        File resource = getResource(request.getPath(), true);

        if (resource == null) {
            // Return 404
        }

        if (!resource.canWrite()) {
            // Return 400
        }

        Response response = new Response();



        return response;
    }

}
