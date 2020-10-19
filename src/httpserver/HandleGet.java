package httpserver;

import java.util.*;

public class HandleGet extends AbstractHandle {

  @Override
  public Response execute(Request request) {
    Response response = new Response();

    String path = request.getPath();
    File file = getResource(path, false);

    if (file.exists() && !file.isDirectory())
    {
      if(request.getHeader("Content-Type") == "text/html") {
        try (Scanner scanner = new Scanner(file)){
          String body = "";

          while(scanner.hasNextLine()) {
            body = body + "\r\n" + scanner.nextLine();
          }

          scanner.close();
          response.setBody(body);
          response.setStatusCode(200);

        } catch(Exception ex) {
          response.setStatusCode(500);
        }

      }
    } else {
      // return 404
      response.setStatusCode(404);
    }

    return response;
  }

}
