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
package ee.sk.hwcrypto.demo.controller;

import ee.sk.hwcrypto.demo.model.Digest;
import ee.sk.hwcrypto.demo.model.FileWrapper;
import ee.sk.hwcrypto.demo.model.Result;
import ee.sk.hwcrypto.demo.model.SigningSessionData;
import ee.sk.hwcrypto.demo.signature.FileSigner;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class SigningController {

    private static final Logger log = LoggerFactory.getLogger(SigningController.class);
    @Autowired
    private SigningSessionData session;
    @Autowired
    private FileSigner signer;

    @RequestMapping(value="/upload", method= RequestMethod.POST)
    public Result handleUpload(@RequestParam MultipartFile file) {
        log.debug("Handling file upload for file "+file.getOriginalFilename());
        try {
            session.setUploadedFile(FileWrapper.create(file));
            return Result.resultOk();
        } catch (IOException e) {
            log.error("Error reading bytes from uploaded file " + file.getOriginalFilename(), e);
        }
        return Result.resultUploadingError();
    }

    @RequestMapping(value="/generateHash", method = RequestMethod.POST)
    public Digest generateHash(@RequestParam String certInHex) {
        log.debug("Generating hash from cert " + StringUtils.left(certInHex, 10) + "...");
        session.setCertInHex(certInHex);
        FileWrapper file = session.getUploadedFile();
        Digest digest = new Digest();
        try {
            digest.setHex(signer.getDataToSign(file, certInHex));
            digest.setResult(Result.OK);
        } catch (FileSigner.HashCalculationException e) {
            log.error("Error Calculating hash", e);
            digest.setResult(Result.ERROR_GENERATING_HASH);
        }
        return digest;
    }

    @RequestMapping(value="/createContainer", method = RequestMethod.POST)
    public Result createContainer(@RequestParam String signatureInHex) {
        log.debug("Creating container for signature " + StringUtils.left(signatureInHex, 10) + "...");
        session.setSignatureInHex(signatureInHex);
        try {
            session.setSignedFile(signer.signDocument(signatureInHex));
            return Result.resultOk();
        } catch (Exception e) {
            log.error("Error Signing document", e);
        }
        return Result.resultSigningError();
    }

}
