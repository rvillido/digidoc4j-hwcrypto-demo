/**
 * DigiDoc4j Hwcrypto Demo
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

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.ArrayUtils;
import org.digidoc4j.DigestAlgorithm;
import org.digidoc4j.exceptions.DigiDoc4JException;

import java.io.FileInputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class TestSigningData {

    public static final String TEST_PKI_CONTAINER = "src/test/resources/signout.p12";
    public static final String TEST_PKI_CONTAINER_PASSWORD = "test";

    public static String getSigningCertificateInHex() {
        try {
            X509Certificate certificate = getSigningCert();
            byte[] derEncodedCertificate = certificate.getEncoded();
            String hexString = Hex.encodeHexString(derEncodedCertificate);
            return hexString;
        } catch (Exception e) {
            throw new RuntimeException("Certificate loading failed", e);
        }
    }

    public static String signDigest(byte[] digestToSign, DigestAlgorithm digestAlgorithm) {
        byte[] signatureValue = sign(digestToSign, digestAlgorithm);
        String hexString = Hex.encodeHexString(signatureValue);
        return hexString;
    }

    private static X509Certificate getSigningCert() {
        return getSigningCert(TEST_PKI_CONTAINER, TEST_PKI_CONTAINER_PASSWORD);
    }

    private static X509Certificate getSigningCert(String pkiContainer, String pkiContainerPassword) {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream stream = new FileInputStream(pkiContainer)) {
                keyStore.load(stream, pkiContainerPassword.toCharArray());
            }
            return (X509Certificate) keyStore.getCertificate("1");
        } catch (Exception e) {
            throw new RuntimeException("Loading signer cert failed");
        }
    }

    private static byte[] sign(byte[] dataToSign, DigestAlgorithm digestAlgorithm) {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream stream = new FileInputStream(TEST_PKI_CONTAINER)) {
                keyStore.load(stream, TEST_PKI_CONTAINER_PASSWORD.toCharArray());
            }
            PrivateKey privateKey = (PrivateKey) keyStore.getKey("1", TEST_PKI_CONTAINER_PASSWORD.toCharArray());
            final String javaSignatureAlgorithm = "NONEwith" + privateKey.getAlgorithm();

            return encrypt(javaSignatureAlgorithm, privateKey, addPadding(dataToSign, digestAlgorithm));
        } catch (Exception e) {
            throw new DigiDoc4JException("Loading private key failed");
        }
    }

    private static byte[] addPadding(byte[] digest, DigestAlgorithm digestAlgorithm) {
        return ArrayUtils.addAll(digestAlgorithm.digestInfoPrefix(), digest);
    }

    private static byte[] encrypt(final String javaSignatureAlgorithm, final PrivateKey privateKey, final byte[] bytes) {
        try {
            java.security.Signature signature = java.security.Signature.getInstance(javaSignatureAlgorithm);
            signature.initSign(privateKey);
            signature.update(bytes);
            final byte[] signatureValue = signature.sign();
            return signatureValue;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
