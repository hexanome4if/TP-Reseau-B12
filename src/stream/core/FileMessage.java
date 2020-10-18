package stream.core;

import java.io.Serializable;

public class FileMessage implements Serializable {

    /**
     * File name
     */
    public String fileName;

    /**
     * File content
     */
    public byte[] fileBytes;

    /**
     * Create a new file message to send over network
     * @param fileName the file name to send
     * @param fileBytes the file content to send
     */
    public FileMessage(String fileName, byte[] fileBytes) {
        this.fileName = fileName;
        this.fileBytes = fileBytes;
    }

    /**
     * Get the file name
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Get the file content
     * @return the file content
     */
    public byte[] getFileBytes() {
        return fileBytes;
    }
}
