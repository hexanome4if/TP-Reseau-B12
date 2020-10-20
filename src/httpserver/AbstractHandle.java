package httpserver;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractHandle {

  public abstract Response execute(Request request);

  protected boolean resourceCreated = false;

  protected File getResource(String path, boolean create) {
    File file = new File("src/httpserver/resources/" + path);
    if (file.exists() && !file.isDirectory()) {
      return file;
    }
    if (create) {
      try {
        if (file.createNewFile()) {
          resourceCreated = true;
          return file;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  protected Response tryToExecute(File resource, Request request) {
    String[] splitResourceName = resource.getName().split("\\.");
    String executableCommand = "";
    switch (splitResourceName[splitResourceName.length - 1]) {
      case "php":
        executableCommand = "php " + resource.getAbsolutePath();
        break;
      case "jar":
        executableCommand = "java -jar " + resource.getAbsolutePath();
        break;
      case "py":
        executableCommand = "python " + resource.getAbsolutePath();
        break;
    }
    if (!executableCommand.equals("")) {
      executableCommand += " " + request.getMethod() + " " + request.getQueryString() + " \"" + request.getBody() + "\"";
      try {
        Process child = Runtime.getRuntime().exec(executableCommand);
        InputStream in = child.getInputStream();

        List<Byte> result = new ArrayList<>();
        int nbBytes;
        do {
          byte[] buffer = new byte[1024];
          nbBytes = in.read(buffer, 0, 1024);
          for(int i = 0; i < nbBytes; ++i) {
            result.add(buffer[i]);
          }
        } while(nbBytes == 1024);

        Response response = new Response();
        response.setStatusCode(200);
        response.setHeader("Content-Type", "text/plain");
        byte[] finalResult = new byte[result.size()];
        for(int i = 0; i < result.size(); ++i) {
          finalResult[i] = result.get(i);
        }
        response.setByteBody(finalResult);
        return response;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

}
