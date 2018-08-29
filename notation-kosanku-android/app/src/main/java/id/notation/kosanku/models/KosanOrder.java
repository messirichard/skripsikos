package id.notation.kosanku.models;

public class KosanOrder {

    private int order_id;
    private int transaction_id;
    private int id_indekos;
    private int id_kamar;
    private int id_user;
    private int amount;
    private String status;
    private String payment_type;

    public KosanOrder() {
    }

    public KosanOrder(int transaction_id, int id_indekos, int id_kamar, int id_user, int amount, String status, String payment_type) {
        this.transaction_id = transaction_id;
        this.id_indekos = id_indekos;
        this.id_kamar = id_kamar;
        this.id_user = id_user;
        this.amount = amount;
        this.status = status;
        this.payment_type = payment_type;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public int getId_indekos() {
        return id_indekos;
    }

    public int getId_kamar() {
        return id_kamar;
    }

    public int getId_user() {
        return id_user;
    }

    public int getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getPayment_type() {
        return payment_type;
    }

    @Override
    public String toString() {
        return "KosanOrder{" +
                "order_id=" + order_id +
                ", transaction_id=" + transaction_id +
                ", id_indekos=" + id_indekos +
                ", id_kamar=" + id_kamar +
                ", id_user=" + id_user +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", payment_type='" + payment_type + '\'' +
                '}';
    }
}
