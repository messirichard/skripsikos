package id.notation.kosanku.models;

import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.TransactionDetails;

public class KosanTransactionData {

	@SerializedName("transaction_details")
	private TransactionDetails transactionDetails;

	@SerializedName("item_details")
	private ItemDetails itemDetails;

	public void setItemDetails(ItemDetails itemDetails){
		this.itemDetails = itemDetails;
	}

	public ItemDetails getItemDetails(){
		return itemDetails;
	}

	public void setTransactionDetails(TransactionDetails transactionDetails){
		this.transactionDetails = transactionDetails;
	}

	public TransactionDetails getTransactionDetails(){
		return transactionDetails;
	}


	@Override
	public String toString() {
		return "KosanTransactionData{" +
				"transactionDetails=" + transactionDetails +
				", itemDetails=" + itemDetails +
				'}';
	}
}