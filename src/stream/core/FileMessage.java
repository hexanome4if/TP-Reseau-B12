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

    public FileMessage(String fileName, byte[] fileBytes) {
        this.fileName = fileName;
        this.fileBytes = fileBytes;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }
}
