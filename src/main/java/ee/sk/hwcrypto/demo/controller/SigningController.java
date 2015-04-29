package ee.sk.hwcrypto.demo.controller;

import ee.sk.hwcrypto.demo.model.Digest;
import ee.sk.hwcrypto.demo.model.FileWrapper;
import ee.sk.hwcrypto.demo.model.Result;
import ee.sk.hwcrypto.demo.model.SigningSessionData;
import ee.sk.hwcrypto.demo.signature.FileSigner;
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
        session.setCertInHex(certInHex);
        FileWrapper file = session.getUploadedFile();
        Digest digest = new Digest();
        try {
            digest.setHex(signer.getDataToSign(file.getBytes(), file.getFileName(), certInHex));
            digest.setResult(Result.OK);
        } catch (FileSigner.HashCalculationException e) {
            log.error("Error Calculating hash", e);
            digest.setResult(Result.ERROR_GENERATING_HASH);
        }
        return digest;
    }

    @RequestMapping(value="/createContainer", method = RequestMethod.POST)
    public Result createContainer(@RequestParam String signatureInHex) {
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
