package httpserver;

import java.io.File;
import java.nio.file.Files;

public class HandleGet extends AbstractHandle {

  @Override
  public Response execute(Request request) {
    Response response = new Response();

    String path = request.getPath();
    File file = getResource(path, false);

    if (file != null)
    {
      try {
        String mimeType = Files.probeContentType(file.toPath());

        byte[] byteBody = Files.readAllBytes(file.toPath());
        response.setHeader("Content-Type", mimeType);
        response.setByteBody(byteBody);
        response.setStatusCode(200);

      } catch(Exception ex) {
        response.setStatusCode(500);
      }
    } else {
      // return 404
      response.setStatusCode(404);
    }

    return response;
  }

}
