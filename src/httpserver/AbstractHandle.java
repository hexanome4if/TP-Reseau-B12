package httpserver;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public abstract class AbstractHandle {

  /**
   * Is the resource has been created
   */
  protected boolean resourceCreated = false;

  /**
   * Process an http request and return a response
   * @param request http request
   * @return a response or null
   */
  public abstract Response execute(Request request);

  /**
   * Get the resource identified by the path and creates it if the boolean create is true
   * @param path path to the resource to create
   * @param create whether to create or not the resource if it does not exists
   * @return a file representing the resource or null
   */
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
