package httpserver.middlewares;

import httpserver.Request;
import httpserver.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExecMiddleware extends AbstractMiddleware {

    @Override
    public Response handle(Request request) {
        File resource =

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
            System.out.println(executableCommand);
            try {
                Process child = Runtime.getRuntime().exec(executableCommand);
                child.waitFor();
                InputStream in = child.getInputStream();

                List<Byte> result = new ArrayList<>();
                int nbBytes;
                do {
                    byte[] buffer = new byte[1024];
                    nbBytes = in.read(buffer, 0, 1024);
                    for (int i = 0; i < nbBytes; ++i) {
                        result.add(buffer[i]);
                    }
                } while (nbBytes == 1024);

                Response response = new Response();
                response.setStatusCode(200);
                response.setHeader("Content-Type", "text/plain");
                byte[] finalResult = new byte[result.size()];
                for (int i = 0; i < result.size(); ++i) {
                    finalResult[i] = result.get(i);
                }
                response.setByteBody(finalResult);
                return response;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
