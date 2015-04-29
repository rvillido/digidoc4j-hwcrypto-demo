package ee.sk.hwcrypto.demo.signature;

import eu.europa.ec.markt.dss.ws.signature.WsDocument;

public class SignedFile {

    private byte[] fileBytes;
    private String fileName;
    private String mimeType;

    public static SignedFile create(WsDocument document) {
        SignedFile file = new SignedFile();
        file.setFileBytes(document.getBytes());
        file.setFileName(document.getName());
        if(document.getMimeType() != null) {
            file.setMimeType(document.getMimeType().getMimeTypeString());
        }
        return file;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
