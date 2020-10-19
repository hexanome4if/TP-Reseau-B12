package httpserver;

public class HandleHead extends AbstractHandle {
    @Override
    public Response execute(Request request) {
        HandleGet getHandler = new HandleGet();
        Response response = getHandler.execute(request);
        response.setBody("");
        return response;
    }
}
