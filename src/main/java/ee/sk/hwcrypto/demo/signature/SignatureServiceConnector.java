package ee.sk.hwcrypto.demo.signature;

import eu.europa.ec.markt.dss.ws.signature.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class SignatureServiceConnector {

    private static final Logger log = LoggerFactory.getLogger(SignatureServiceConnector.class);

    @Value("${dss.service.url}")
    private String dssServiceUrl;

    @PostConstruct
    public void initService() {
        log.info("DSS service url is " + dssServiceUrl);
    }

    public byte[] getDataToSign(WsDocument wsDocument, WsParameters wsParameters) throws GettingDataToSignException {
        log.debug("Getting data to sign from " + dssServiceUrl);
        try {
            SignatureService_Service.setROOT_SERVICE_URL(dssServiceUrl);
            SignatureService_Service signatureService_service = new SignatureService_Service();
            SignatureService signatureServiceImplPort = signatureService_service.getSignatureServiceImplPort();
            return signatureServiceImplPort.getDataToSign(wsDocument, wsParameters);
        } catch (DSSException_Exception e) {
            log.error("Failed to get data for signing from DSS Web Service",e);
            throw new GettingDataToSignException();
        }
    }

    public WsDocument signDocument(WsDocument document, WsParameters parameters, byte[] signature) throws SigningFailedException {
        log.debug("Signing document at " + dssServiceUrl);
        try {
            SignatureService_Service.setROOT_SERVICE_URL(dssServiceUrl);
            SignatureService_Service signatureService_service = new SignatureService_Service();
            SignatureService signatureServiceImplPort = signatureService_service.getSignatureServiceImplPort();
            return signatureServiceImplPort.signDocument(document, parameters, signature);
        } catch (DSSException_Exception e) {
            log.error("Failed to sign document in DSS Web Service", e);
            throw new SigningFailedException();
        }
    }

    public static class GettingDataToSignException extends RuntimeException {
    }

    public static class SigningFailedException extends RuntimeException {
    }
}
