/**
 * DSS Hwcrypto Demo
 *
 * Copyright (c) 2015 Estonian Information System Authority
 *
 * The MIT License (MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ee.sk.hwcrypto.demo.model;

import eu.europa.ec.markt.dss.ws.signature.WsDocument;
import eu.europa.ec.markt.dss.ws.signature.WsParameters;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SigningSessionData implements Serializable {

    private WsDocument signatureContainer;
    private WsParameters signatureParameters;
    private FileWrapper uploadedFile;
    private FileWrapper signedFile;
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

    public FileWrapper getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(FileWrapper uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public FileWrapper getSignedFile() {
        return signedFile;
    }

    public void setSignedFile(FileWrapper signedFile) {
        this.signedFile = signedFile;
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
