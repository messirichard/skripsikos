package id.notation.kosanku.models;

import com.google.gson.annotations.SerializedName;

public class TransactionDetails{

	@SerializedName("gross_amount")
	private int grossAmount;

	@SerializedName("order_id")
	private Object orderId;

	public void setGrossAmount(int grossAmount){
		this.grossAmount = grossAmount;
	}

	public int getGrossAmount(){
		return grossAmount;
	}

	public void setOrderId(Object orderId){
		this.orderId = orderId;
	}

	public Object getOrderId(){
		return orderId;
	}

	@Override
 	public String toString(){
		return 
			"TransactionDetails{" + 
			"gross_amount = '" + grossAmount + '\'' + 
			",order_id = '" + orderId + '\'' + 
			"}";
		}
}