package ee.sk.hwcrypto.demo.model;

public class Result {

    public static final String OK = "ok";
    public static final String ERROR_GENERATING_HASH = "error_generating_hash";
    public static final String ERROR_UPLOADING = "error_uploading_file";
    public static final String ERROR_SIGNING = "error_signing_file";

    private String result;

    public Result() {
    }

    public Result(String result) {
        this.result = result;
    }

    public static Result resultOk() {
        return new Result(OK);
    }

    public static Result resultUploadingError() {
        return new Result(ERROR_UPLOADING);
    }

    public static Result resultSigningError() {
        return new Result(ERROR_SIGNING);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
