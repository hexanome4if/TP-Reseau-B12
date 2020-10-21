package stream.core.infos;

import java.io.Serializable;

/**
 *
 */
public class FileMessageInfo implements Serializable {
    /**
     * Message file name
     */
    private final String fileName;

    /**
     * Message file content
     */
    private final byte[] fileContent;

    /**
     * Create a new file message info to send to the clients
     * @param fileName the file name
     * @param fileContent the file content
     */
    public FileMessageInfo(String fileName, byte[] fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

    /**
     * Get the file name
     * @return file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Get the file content
     * @return file content
     */
    public byte[] getFileContent() {
        return fileContent;
    }
}
