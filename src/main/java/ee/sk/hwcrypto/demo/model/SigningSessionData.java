package ee.sk.hwcrypto.demo.model;

import eu.europa.ec.markt.dss.ws.signature.WsDocument;
import eu.europa.ec.markt.dss.ws.signature.WsParameters;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SigningSessionData {

    private WsDocument signatureContainer;
    private WsParameters signatureParameters;
    private byte[] file;
    private String fileName;
    private String fileContentType;
    private String certInHex;
    private String signatureInHex;
    private String digestInHex;

    public WsDocument getSignatureContainer() {
        return signatureContainer;
    }

    public void setSignatureContainer(WsDocument signatureContainer) {
        this.signatureContainer = signatureContainer;
    }

    public WsParameters getSignatureParameters() {
        return signatureParameters;
    }

    public void setSignatureParameters(WsParameters signatureParameters) {
        this.signatureParameters = signatureParameters;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getCertInHex() {
        return certInHex;
    }

    public void setCertInHex(String certInHex) {
        this.certInHex = certInHex;
    }

    public String getDigestInHex() {
        return digestInHex;
    }

    public void setDigestInHex(String digestInHex) {
        this.digestInHex = digestInHex;
    }

    public void setSignatureInHex(String signatureInHex) {
        this.signatureInHex = signatureInHex;
    }

    public String getSignatureInHex() {
        return signatureInHex;
    }
}
