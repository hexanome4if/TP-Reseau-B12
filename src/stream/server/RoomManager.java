package stream.server;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class RoomManager {
    /**
     * Lock to handle concurrency on this class
     */
    private static final Semaphore lock = new Semaphore(1);
    /**
     * Object output stream to save rooms on disk
     */
    private static ObjectOutputStream oos;
    /**
     * List of rooms
     */
    public static List<RoomEntry> rooms = new ArrayList<>();

    /**
     * Initialize the room manager (get the saved rooms and open the object output stream)
     */
    public static void init() {
        System.out.println("Init room manager");
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("rooms.ser"));
            while (true) {
                try {
                    rooms.add(new RoomEntry(((RoomSerializable) ois.readObject()).getRoomName()));
                    System.out.println("Found a room");
                } catch (EOFException ex) {
                    break;
                }
            }
        } catch (EOFException e) {
            // Do nothing
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            oos = new ObjectOutputStream(new FileOutputStream("rooms.ser"));
            for(RoomEntry room: rooms) {
                oos.writeObject(new RoomSerializable(room.getName()));
            }
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lock rooms when we need to use the room list
     * @throws InterruptedException
     */
    public static void lockRooms() throws InterruptedException {
        lock.acquire();
    }

    /**
     * Release rooms when we are done using the room list
     */
    public static void releaseRooms() {
        lock.release();
    }

    /**
     * Add a room to the room list (concurrency is handled automatically)
     * @param roomName the room name to add
     * @return whether the action went good or not
     */
    public static boolean addRoom(String roomName) {
        try {
            lockRooms();
            if (rooms.stream().filter(roomEntry -> roomEntry.getName().equals(roomName)).toArray().length == 0) {
                rooms.add(new RoomEntry(roomName));
                releaseRooms();
                try {
                    oos.writeObject(new RoomSerializable(roomName));
                    oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            releaseRooms();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void joinRoom(String roomName, String userId) {
        try {
            lockRooms();
            for(RoomEntry room: rooms) {
                if (room.getName().equals(roomName)) {
                    room.addUser(userId);
                    break;
                }
            }
            releaseRooms();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void leaveRoom(String roomName, String userId) {
        try {
            lockRooms();
            for (RoomEntry room : rooms) {
                if (room.getName().equals(roomName)) {
                    room.removeUser(userId);
                    break;
                }
            }
            releaseRooms();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
