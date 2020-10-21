package stream.server;

import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author polo
 */
public class ClientContainer {
    /**
     * Connected clients list
     */
    private static final Map<String, ClientData> clients = new HashMap<>();

    /**
     * Add the new client to the list of connected clients
     * @param socket client socket
     */
    public static synchronized void addClient(Socket socket) {
        ClientData clientData = new ClientData(socket);
        clients.put(getKey(socket), clientData);
    }

    /**
     * Get the connected client identified by it's socket
     * @param socket client socket
     * @return ClientData if found null if not
     */
    public static synchronized ClientData getClient(Socket socket) {
        String key = getKey(socket);
        if (clients.containsKey(key)) {
            return clients.get(key);
        }
        return null;
    }

    /**
     * Remove a client from the list of connected clients
     * @param socket client socket
     */
    public static synchronized void removeClient(Socket socket) {
        String key = getKey(socket);
        clients.remove(key);
    }

    /**
     * Get every connected clients
     * @return collection of clients
     */
    public static synchronized Collection<ClientData> getClients() {
        return clients.values();
    }

    /**
     * Get the client key defined by it's IP + Port
     * @param socket client socket
     * @return Key identifier of a client
     */
    private static String getKey(Socket socket) {
        return socket.getInetAddress() + ":" + socket.getPort();
    }
}
