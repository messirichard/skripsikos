package id.notation.kosanku.models;

import com.midtrans.sdk.corekit.models.snap.Transaction;


public class KosanTransaction {
    KosanTransactionData data;
    int status;

    public KosanTransactionData getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }


}
