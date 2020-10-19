package httpserver;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class HandleGet extends AbstractHandle {

  @Override
  public Response execute(Request request) {
    Response response = new Response();

    String path = request.getPath();
    File file = getResource(path, false);

    if (file != null)
    {

      try (Scanner scanner = new Scanner(file)){
        String mimeType = Files.probeContentType(file.toPath());
        String body = "";

        while(scanner.hasNextLine()) {
          body = body + scanner.nextLine() + "\r\n";
        }

        scanner.close();
        response.setHeader("Content-Type", mimeType);
        response.setBody(body);
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
