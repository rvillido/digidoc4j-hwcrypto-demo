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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;

public class FileWrapper implements Serializable{
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