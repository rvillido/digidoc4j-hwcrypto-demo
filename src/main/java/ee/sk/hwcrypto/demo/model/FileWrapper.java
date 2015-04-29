package ee.sk.hwcrypto.demo.model;

import eu.europa.ec.markt.dss.ws.signature.WsDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FileWrapper {
    private byte[] bytes;
    private String fileName;
    private String mimeType;

    public static FileWrapper create(MultipartFile multipartFile) throws IOException {
        FileWrapper file = new FileWrapper();
        file.setBytes(multipartFile.getBytes());
        file.setFileName(multipartFile.getOriginalFilename());
        file.setMimeType(multipartFile.getContentType());
        return file;
    }

    public static FileWrapper create(WsDocument document) {
        FileWrapper file = new FileWrapper();
        file.setBytes(document.getBytes());
        file.setFileName(document.getName());
        if(document.getMimeType() != null) {
            file.setMimeType(document.getMimeType().getMimeTypeString());
        }
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}