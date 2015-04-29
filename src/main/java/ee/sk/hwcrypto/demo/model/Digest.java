package ee.sk.hwcrypto.demo.model;

public class Digest extends Result{

    private String hex;

    public Digest() {
    }

    public Digest(String hex) {
        this.hex = hex;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }
}
