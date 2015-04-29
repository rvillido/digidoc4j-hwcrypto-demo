package ee.sk.hwcrypto.demo.model;

public class FileWrapper {
    private byte[] file;
    private String fileName;
    private String fileContentType;

    public FileWrapper() {
    }

    public FileWrapper(byte[] file, String fileName, String fileContentType) {
        this.file = file;
        this.fileName = fileName;
        this.fileContentType = fileContentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getBytes() {
        return file;
    }

    public void setBytes(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }
}