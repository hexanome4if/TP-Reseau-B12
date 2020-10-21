/**
 * HandleHead
 * Execute the HEAD request
 * Date: 17/10/2020
 * Authors: Paul Moine and Fabien Narboux
 */
package httpserver;

/**
 *
 */
public class HandleHead extends AbstractHandle {

    /**
    * Execute the HEAD request
    * @param request request received
    **/
    @Override
    public Response execute(Request request) {
        HandleGet getHandler = new HandleGet();
        Response response = getHandler.execute(request);
        response.setBody("");
        response.setByteBody(new byte []{});
        return response;
    }
}
