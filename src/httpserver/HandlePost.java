package httpserver;

import java.io.*;

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
            System.out.println("Cant read");
            response.setStatusCode(400);
            return response;
        }

        if (!request.getHeader("Content-Type").equals("text/plain")) {
            System.out.println("Wrong type");
            response.setStatusCode(400);
            return response;
        }

        if (request.getBody() == null || request.getBody().equals("")) {
            System.out.println("No body");
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
