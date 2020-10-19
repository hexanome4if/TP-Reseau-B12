package httpserver;

import java.io.File;
import java.io.IOException;

public class HandlePost extends AbstractHandle {

    @Override
    public Response execute(Request request) {
        Response response = new Response();
        try {
            File resource = new File("src/httpserver/resources/" + request.getPath());

            if (resource.exists() && !resource.isDirectory()) {

            } else {
                if (resource.createNewFile()) {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
