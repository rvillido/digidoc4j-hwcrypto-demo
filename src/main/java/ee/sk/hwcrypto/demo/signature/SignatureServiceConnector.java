package ee.sk.hwcrypto.demo.signature;

import eu.europa.ec.markt.dss.ws.signature.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SignatureServiceConnector {

    private static final Logger log = LoggerFactory.getLogger(SignatureServiceConnector.class);

    //TODO load from a property file
    private static final String DSS_URL = "http://dss.nowina.lu/dss-webapp/wservice";

    public byte[] getDataToSign(WsDocument wsDocument, WsParameters wsParameters) throws GettingDataToSignException {
        try {
            SignatureService_Service.setROOT_SERVICE_URL(DSS_URL);
            SignatureService_Service signatureService_service = new SignatureService_Service();
            SignatureService signatureServiceImplPort = signatureService_service.getSignatureServiceImplPort();
            return signatureServiceImplPort.getDataToSign(wsDocument, wsParameters);
        } catch (DSSException_Exception e) {
            log.error("Failed to get data for signing from DSS Web Service",e);
            throw new GettingDataToSignException();
        }
    }

    public static class GettingDataToSignException extends RuntimeException {
    }
}
