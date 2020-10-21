/**
 * HandlePut
 * Execute a PUT request
 * Date: 17/10/2020
 * Authors: Paul Moine and Fabien Narboux
 */

package httpserver;

import java.io.*;

/**
 *
 */
public class HandlePut extends AbstractHandle {

    /**
    * Execute the PUT request
    * @param request request received
    **/
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

        if (request.getBody() == null || request.getBody().equals("")) {
            response.setStatusCode(400);
            return response;
        }

        if(request.getHeader("Content-Type").contains("text/")) {

          try (BufferedWriter writer = new BufferedWriter(new FileWriter(resource, false))) {
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

        } else {

          try (FileOutputStream writer = new FileOutputStream(resource.getAbsolutePath())) {
              writer.write(request.getByteBody());
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

        }

        return response;
    }
}
