package httpserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HandlePost extends AbstractHandle {

    @Override
    public Response execute(Request request) {
        File resource = getResource(request.getPath(), true);

        Response response = new Response();

        if (resource == null) {
            response.setStatusCode(404);
            return response;
        }

        if (!resource.canWrite()) {
            response.setStatusCode(400);
            return response;
        }

        if (!request.getHeader("Content-Type").equals("text/plain")) {
            response.setStatusCode(400);
            return response;
        }

        if (request.getBody() == null || request.getBody().equals("")) {
            response.setStatusCode(400);
            return response;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resource, true))) {
            writer.write(request.getBody());
            writer.flush();

            // All good
            if (resourceCreated) {
                response.setStatusCode(201);
                response.setHeader("Location", request.getPath());
            } else {
                response.setStatusCode(204);
            }

        } catch (IOException e) {
            response.setStatusCode(500);
        }

        return response;
    }

}
