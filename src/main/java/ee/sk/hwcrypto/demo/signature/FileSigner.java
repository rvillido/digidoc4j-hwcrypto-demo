package ee.sk.hwcrypto.demo.signature;

import eu.europa.ec.markt.dss.DSSXMLUtils;
import eu.europa.ec.markt.dss.signature.DSSDocument;
import eu.europa.ec.markt.dss.signature.InMemoryDocument;
import eu.europa.ec.markt.dss.signature.MimeType;
import eu.europa.ec.markt.dss.ws.signature.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;

@Service
public class FileSigner {

    private static ObjectFactory FACTORY = new ObjectFactory();
    @Autowired
    SignatureServiceConnector signatureServiceConnector;

    public String getDataToSign(byte[] fileBytes, String fileName, String certificateInHex) throws UnsupportedEncodingException, DSSException_Exception, NoSuchProviderException, NoSuchAlgorithmException {
        WsDocument wsDocument = createDocumentToSign(fileBytes, fileName);
        WsParameters wsParameters = createSignatureParameters(certificateInHex);
        byte[] dataToSign = signatureServiceConnector.getDataToSign(wsDocument, wsParameters);
        byte[] hashToSign = calculateHash(dataToSign, "SHA-256");
        String dataToSignInHex = DatatypeConverter.printHexBinary(hashToSign);
        return dataToSignInHex;
    }

    private WsDocument createDocumentToSign(byte[] fileBytes, String fileName) {
        DSSDocument dssDocument = new InMemoryDocument(fileBytes, fileName);
        WsDocument wsDocument = new WsDocument();
        wsDocument.setBytes(dssDocument.getBytes());
        wsDocument.setName(dssDocument.getName());
        wsDocument.setAbsolutePath(dssDocument.getAbsolutePath());
        MimeType mimeType = dssDocument.getMimeType();
        eu.europa.ec.markt.dss.ws.signature.MimeType wsMimeType = FACTORY.createMimeType();
        String mimeTypeString = mimeType.getMimeTypeString();
        wsMimeType.setMimeTypeString(mimeTypeString);
        wsDocument.setMimeType(wsMimeType);
        return wsDocument;
    }

    private WsParameters createSignatureParameters(String certificateInHex) {
        WsParameters wsParameters = new WsParameters();
        wsParameters.setSignatureLevel(SignatureLevel.ASiC_E_BASELINE_B);
        wsParameters.setSignaturePackaging(SignaturePackaging.DETACHED);
        wsParameters.setEncryptionAlgorithm(EncryptionAlgorithm.RSA);
        wsParameters.setAsicMimeType("application/vnd.etsi.asic-e+zip");
        //wsParameters.setAsicSignatureForm(SignatureForm.ASiC_E);
        wsParameters.setDigestAlgorithm(DigestAlgorithm.SHA256);
        XMLGregorianCalendar xmlGregorianCalendar = DSSXMLUtils.createXMLGregorianCalendar(new Date());
        wsParameters.setSigningDate(xmlGregorianCalendar);
        byte[] bincert = DatatypeConverter.parseHexBinary(certificateInHex);
        wsParameters.setSigningCertificateBytes(bincert);
        wsParameters.setDeterministicId("S0");
        return wsParameters;
    }

    private byte[] calculateHash(byte[] data, String digType) throws NoSuchProviderException, NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance(digType, "BC");
        sha.update(data);
        return sha.digest();
    }
}
