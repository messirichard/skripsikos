package id.notation.kosanku.models;

public class KosanOrderItem {

    private KosanOrder data;
    private int Status;

    public KosanOrder getData() {
        return data;
    }

    public void setData(KosanOrder data) {
        this.data = data;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
