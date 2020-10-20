package httpserver;

import java.io.*;

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

}
