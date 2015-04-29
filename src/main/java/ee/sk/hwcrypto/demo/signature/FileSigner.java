package ee.sk.hwcrypto.demo.signature;

import ee.sk.hwcrypto.demo.model.FileWrapper;
import ee.sk.hwcrypto.demo.model.SigningSessionData;
import eu.europa.ec.markt.dss.DSSXMLUtils;
import eu.europa.ec.markt.dss.signature.DSSDocument;
import eu.europa.ec.markt.dss.signature.InMemoryDocument;
import eu.europa.ec.markt.dss.signature.MimeType;
import eu.europa.ec.markt.dss.ws.signature.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;

@Service
public class FileSigner {

    private static final Logger log = LoggerFactory.getLogger(FileSigner.class);
    private static final ObjectFactory FACTORY = new ObjectFactory();
    private static final String DIGEST_ALGORITHM = "SHA-256";
    private static final DigestAlgorithm DIGEST_TYPE = DigestAlgorithm.SHA256;
    @Autowired
    SignatureServiceConnector signatureServiceConnector;
    @Autowired
    SigningSessionData sessionData;

    public String getDataToSign(byte[] fileBytes, String fileName, String certificateInHex) {
        WsDocument containerToSign = createContainerToSign(fileBytes, fileName);
        WsParameters signatureParameters = createSignatureParameters(certificateInHex);
        byte[] dataToSign = signatureServiceConnector.getDataToSign(containerToSign, signatureParameters);
        byte[] hashToSign = calculateHash(dataToSign, DIGEST_ALGORITHM);
        String dataToSignInHex = DatatypeConverter.printHexBinary(hashToSign);
        sessionData.setSignatureContainer(containerToSign);
        sessionData.setSignatureParameters(signatureParameters);
        return dataToSignInHex;
    }

    public FileWrapper signDocument(String signatureInHex) {
        WsDocument containerToSign = sessionData.getSignatureContainer();
        WsParameters signatureParameters = sessionData.getSignatureParameters();
        byte[] signatureBytes = DatatypeConverter.parseHexBinary(signatureInHex);
        WsDocument signedDocument = signatureServiceConnector.signDocument(containerToSign, signatureParameters, signatureBytes);
        return FileWrapper.create(signedDocument);
    }

    private WsDocument createContainerToSign(byte[] fileBytes, String fileName) {
        DSSDocument fileToSign = new InMemoryDocument(fileBytes, fileName);
        WsDocument container = new WsDocument();
        container.setBytes(fileToSign.getBytes());
        container.setName(fileToSign.getName());
        container.setAbsolutePath(fileToSign.getAbsolutePath());
        MimeType mimeType = fileToSign.getMimeType();
        eu.europa.ec.markt.dss.ws.signature.MimeType wsMimeType = FACTORY.createMimeType();
        String mimeTypeString = mimeType.getMimeTypeString();
        wsMimeType.setMimeTypeString(mimeTypeString);
        container.setMimeType(wsMimeType);
        return container;
    }

    private WsParameters createSignatureParameters(String certificateInHex) {
        WsParameters parameters = new WsParameters();
        parameters.setSignatureLevel(SignatureLevel.ASiC_E_BASELINE_B);
        parameters.setSignaturePackaging(SignaturePackaging.DETACHED);
        parameters.setEncryptionAlgorithm(EncryptionAlgorithm.RSA);
        parameters.setAsicMimeType("application/vnd.etsi.asic-e+zip");
        parameters.setDigestAlgorithm(DIGEST_TYPE);
        parameters.setSigningDate(DSSXMLUtils.createXMLGregorianCalendar(new Date()));
        byte[] certificateBytes = DatatypeConverter.parseHexBinary(certificateInHex);
        parameters.setSigningCertificateBytes(certificateBytes);
        parameters.setDeterministicId("S0");
        return parameters;
    }

    private byte[] calculateHash(byte[] data, String digestType) {
        try {
            MessageDigest sha = MessageDigest.getInstance(digestType, "BC");
            sha.update(data);
            return sha.digest();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            log.error("Error calculating hash with " + digestType, e);
            throw new HashCalculationException(e);
        }
    }

    public static class HashCalculationException extends RuntimeException {
        public HashCalculationException(Throwable cause) {
            super(cause);
        }
    }
}
