/**
 * SenderThread
 * Sender thread for a multicast UDP client
 * Date: 17/10/2020
 * Authors: Paul Moine and Fabien Narboux
 */

package httpserver;

import java.io.File;

/**
 *
 */
public class HandleDelete extends AbstractHandle {
    /**
    * Execute the DELETE request
    * @param request request received
    **/
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
