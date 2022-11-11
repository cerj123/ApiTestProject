package api;

public class UnsuccessfulReg {
    private String error;

    public UnsuccessfulReg() {
        super();
    }

    public UnsuccessfulReg(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
