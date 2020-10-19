package stream.core.requests;

import java.io.Serializable;

public class FileMessageRequest implements Serializable {
    /**
     * The file name to send
     */
    private final String fileName;

    /**
     * File content
     */
    private final byte[] fileData;

    /**
     * Create a new file message request to send to the server
     * @param fileName the file name
     * @param fileData the file content
     */
    public FileMessageRequest(String fileName, byte[] fileData) {
        this.fileName = fileName;
        this.fileData = fileData;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public String getFileName() {
        return fileName;
    }
}
