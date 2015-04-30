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
package ee.sk.hwcrypto.demo.signature;

import ee.sk.hwcrypto.demo.model.FileWrapper;
import ee.sk.hwcrypto.demo.model.SigningSessionData;
import eu.europa.ec.markt.dss.ws.signature.MimeType;
import eu.europa.ec.markt.dss.ws.signature.WsDocument;
import eu.europa.ec.markt.dss.ws.signature.WsParameters;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileSignerTest {

    static final String TEST_CERT = "308204FD308203E5A00302010202105F9C949FB1E2DF3F5176756F97FEE295300D06092A864886F70D0101050500306C310B300906035504061302454531223020060355040A0C19415320536572746966697473656572696D69736B65736B7573311F301D06035504030C1654455354206F66204553544549442D534B20323031313118301606092A864886F70D0109011609706B6940736B2E6565301E170D3133303432333131353030375A170D3138303132393231353935395A30819E310B3009060355040613024545310F300D060355040A0C06455354454944311A3018060355040B0C116469676974616C207369676E61747572653126302406035504030C1D4DC3844E4E494B2C4D4152492D4C4949532C34373130313031303033333110300E06035504040C074DC3844E4E494B31123010060355042A0C094D4152492D4C494953311430120603550405130B343731303130313030333330820122300D06092A864886F70D01010105000382010F003082010A02820100A8DF97BCCEF6DC6222D62289393EC771133F36E23AD52534F76F2CE8D62128AB219F67CCE751EBC8DA1D3D93A7A5BA48372236E59F1C7FA0BF5BE4967FF9CF7949B9809CDCBA6FB44AD7B921D26B7E8FA7C095479FB5DC91C83AAFFA8EC2CE36A1DE3A2170DC217E3A944ACA3E95D59D55E4B488188CE634C63FCB1E9EFBEAF0A57F7944EF4A573981B204FE59F7058BF6EC4C16339413CB012488A247133D117CE4DFB9352671D721CC593B0CC1BD602FA0950C19D1C0A359CCF1F4A733FBABD4E69BCAD390E7ECF44995C3B0EB579CEC285C8D355F0EF94CEBC30E9E9EE06935574CC0925CE168BC2BC10730BD1B2FFBE9E7F13FD1044D1B019CDBBD2AFC81020440000081A38201663082016230090603551D1304023000300E0603551D0F0101FF0404030206403081990603551D2004819130818E30818B060A2B06010401CE1F030101307D305806082B06010505070202304C1E4A00410069006E0075006C0074002000740065007300740069006D006900730065006B0073002E0020004F006E006C007900200066006F0072002000740065007300740069006E0067002E302106082B060105050702011615687474703A2F2F7777772E736B2E65652F6370732F301D0603551D0E0416041489EDF32A23C550F400DB56AB0736CFAF26613C6A302206082B06010505070103041630143008060604008E4601013008060604008E460104301F0603551D2304183016801441B6FEC5B1B1B453138CFAFA62D0346D6D22340A30450603551D1F043E303C303AA038A0368634687474703A2F2F7777772E736B2E65652F7265706F7369746F72792F63726C732F746573745F657374656964323031312E63726C300D06092A864886F70D01010505000382010100CA6F7AECFE9D00646E06A90160F2CDBE9FFEC830D625E7F9502766B3EB839E29C35C76E4D95B049A09D82FC4064130A320465267D0294ED9D99CACD90E3A4891ACFA7E0DEC6DC12CB8658E729F5E47AD40D8749BE302AE355D0BA0232F479F172DF633FBA3EA94C1600B81C5FC14A4A01F5AC66F63D5640AE9EBFCAB701E39EE34484DC7CF918551192DFA406FE0D96CC7B7F229B2ABC1998FA8B405C89B3797AE963C6A8BB07DCC8772F1EFF403FCDAE26914FB00BD2FA4A8222379ED61DA1DEC705E483379824E550E870F58DA1ED07D1CAC27FB7DFC4863245D9CEE769412FB4544354966140758006614F87281A2B33F16C6AF29A80E6B1DF039F2CDF738";
    static final String DATA_TO_SIGN = "test";
    static final String HASH_TO_SIGN_IN_HEX = "9F86D081884C7D659A2FEAA0C55AD015A3BF4F1B2B0B822CD15D6C15B0F00A08";
    static final String SIGNATURE_IN_HEX = "64CA59903D10CFE72F67469B799911770A314CFCE1CF14B8A0ECF3D62BEA73A3ED30CCCC297008DEE8E77BDA8C874612A6DD533B115163168E433A276B4B7778D27683C9DB52FF3280A9C40564CC1CB2E42A37CFFFA2B4F93D238246A2EF17B391E7E85082198984721B3504DA6D148C062053122B95DF908796F3EC92C35301D6504FB3BD5E9CBEDD8FAC3FFD721BC29289C6870D7550D0AA815A8F9B52DD473997F854430157AD35D44AC0C2EED667800488BE95EFD99CCEC1C916FF1D0661B424FB899422224DCC9F4B4933CFEB0C5C1BABDF49F2AF4B2E49ACAB8877DEFD50673A6C9A1C8D010F368D154C473FFD220C8E6F00D8F0428E609B38E5B37307";
    private SignatureServiceConnectorSpy signatureServiceConnectorSpy;
    private WsDocument signedDocument;
    private FileSigner fileSigner;
    private SigningSessionData sessionData;

    @Before
    public void setUp() throws Exception {
        fileSigner = new FileSigner();
        signedDocument = new WsDocument();
        sessionData = new SigningSessionData();
        signatureServiceConnectorSpy = new SignatureServiceConnectorSpy();
        fileSigner.sessionData = sessionData;
        fileSigner.signatureServiceConnector = signatureServiceConnectorSpy;
    }

    @Test
    public void testGettingDataToSign() throws Exception {
        FileWrapper file = createFile("test.txt", "Test data to sign");
        String dataToSign = fileSigner.getDataToSign(file, TEST_CERT);
        assertEquals(HASH_TO_SIGN_IN_HEX, dataToSign);
        assertNotNull(sessionData.getSignatureContainer());
        assertNotNull(sessionData.getSignatureParameters());
    }

    @Test
    public void testGettingSignedDocument() throws Exception {
        signedDocument.setName("test.name");
        signedDocument.setMimeType(createMimeType("mime/type"));
        signedDocument.setBytes("testData".getBytes());
        FileWrapper fileWrapper = fileSigner.signDocument(SIGNATURE_IN_HEX);
        assertEquals("test.name", fileWrapper.getFileName());
        assertEquals("mime/type", fileWrapper.getMimeType());
        assertArrayEquals("testData".getBytes(), fileWrapper.getBytes());
    }

    @Test
    public void testSigningDocument() throws Exception {
        FileWrapper file = createFile("test.txt", "Test data to sign");
        String dataToSign = fileSigner.getDataToSign(file, TEST_CERT);
        assertEquals(HASH_TO_SIGN_IN_HEX, dataToSign);
        FileWrapper fileWrapper = fileSigner.signDocument(SIGNATURE_IN_HEX);
        assertNotNull(fileWrapper);
        assertSame(sessionData.getSignatureContainer(), signatureServiceConnectorSpy.usedDocument);
        assertSame(sessionData.getSignatureParameters(), signatureServiceConnectorSpy.usedParameters);
    }

    private FileWrapper createFile(String name, String data) {
        FileWrapper file = new FileWrapper();
        file.setBytes(data.getBytes());
        file.setFileName(name);
        return file;
    }

    private MimeType createMimeType(String mimeTypeString) {
        MimeType mimeType = new MimeType();
        mimeType.setMimeTypeString(mimeTypeString);
        return mimeType;
    }

    private class SignatureServiceConnectorSpy extends SignatureServiceConnector {
        WsDocument usedDocument;
        WsParameters usedParameters;
        byte[] usedSignature;

        @Override
        public byte[] getDataToSign(WsDocument wsDocument, WsParameters wsParameters) {
            return DATA_TO_SIGN.getBytes();
        }

        @Override
        public WsDocument signDocument(WsDocument document, WsParameters parameters, byte[] signature) throws SigningFailedException {
            usedDocument = document;
            usedParameters = parameters;
            usedSignature = signature;
            return signedDocument;
        }
    }
}
