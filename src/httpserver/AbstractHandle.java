package httpserver;

import java.io.File;
import java.io.IOException;

public abstract class AbstractHandle {

  public abstract Response execute(Request request);

  protected File getResource(String path, boolean create) {
    File file = new File("src/httpserver/resources/" + path);
    if (file.exists() && !file.isDirectory()) {
      return file;
    }
    if (create) {
      try {
        if (file.createNewFile()) {
          return file;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

}
