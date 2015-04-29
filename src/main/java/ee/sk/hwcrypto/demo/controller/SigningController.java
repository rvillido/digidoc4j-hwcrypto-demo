package ee.sk.hwcrypto.demo.controller;

import ee.sk.hwcrypto.demo.model.Digest;
import ee.sk.hwcrypto.demo.model.FileWrapper;
import ee.sk.hwcrypto.demo.model.Result;
import ee.sk.hwcrypto.demo.model.SigningSessionData;
import ee.sk.hwcrypto.demo.signature.FileSigner;
import eu.europa.ec.markt.dss.ws.signature.DSSException_Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@RestController
public class SigningController {

    @Autowired
    private SigningSessionData session;

    @Autowired
    private FileSigner signer;

    @RequestMapping(value="/upload", method= RequestMethod.POST)
    public Result handleUpload(@RequestParam MultipartFile file) {
        try {
            session.setFile(new FileWrapper(file.getBytes(), file.getOriginalFilename(), file.getContentType()));
            return Result.resultOk();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.resultUploadingError();
    }

    @RequestMapping(value="/generateHash", method = RequestMethod.POST)
    public Digest generateHash(@RequestParam String certInHex) {
        session.setCertInHex(certInHex);
        FileWrapper file = session.getFile();
        Digest digest = new Digest();
        try {
            digest.setHex(signer.getDataToSign(file.getBytes(), file.getFileName(), certInHex));
            digest.setResult(Result.OK);
        } catch (UnsupportedEncodingException | DSSException_Exception | NoSuchProviderException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            digest.setResult(Result.ERROR_GENERATING_HASH);
        }
        return digest;
    }

    @RequestMapping(value="/createContainer", method = RequestMethod.POST)
    public Result createContainer(@RequestParam String signatureInHex) {
        session.setSignatureInHex(signatureInHex);
        return Result.resultOk();
    }

    @RequestMapping("/getOk")
    public Result getOk() {
        return Result.resultOk();
    }

}
