package api;

public class UnsuccessReg {
    private String error;

    public UnsuccessReg() {
        super();
    }

    public UnsuccessReg(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
