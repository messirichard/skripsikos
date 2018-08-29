package id.notation.kosanku;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;

import id.notation.kosanku.models.KosanOrder;
import id.notation.kosanku.models.KosanTransactionData;
import id.notation.kosanku.utils.SdkConfig;
import id.notation.kosanku.utils.SharedPreferenceManager;
import id.notation.kosanku.utils.api.ApiService;
import id.notation.kosanku.utils.api.UtilsApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends Activity implements TransactionFinishedCallback {

    private String TAG = "Payment Activity";
    ApiService apiService;
    KosanTransactionData kosanTransactionData;
    SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        Gson gson = new Gson();
        apiService = UtilsApi.getAPIService();

        Log.d(TAG, "onCreate: " + intent.getStringExtra("transaction_data").toString());
        kosanTransactionData = gson.fromJson(intent.getStringExtra("transaction_data"), KosanTransactionData.class);
        sharedPreferenceManager = new SharedPreferenceManager(this);

        initMidtransSdk();
        UIKitCustomSetting uisetting = new UIKitCustomSetting();
        uisetting.setSkipCustomerDetailsPages(true);
        uisetting.setShowPaymentStatus(true);
        MidtransSDK.getInstance().setUIKitCustomSetting(uisetting);
        MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest(kosanTransactionData));
        MidtransSDK.getInstance().startPaymentUiFlow(PaymentActivity.this);

    }

    private void initMidtransSdk() {
        String client_key = SdkConfig.MERCHANT_CLIENT_KEY;
        String base_url = SdkConfig.MERCHANT_BASE_CHECKOUT_URL;

        SdkUIFlowBuilder.init()
                .setClientKey(client_key) // client_key is mandatory
                .setContext(this) // context is mandatory
                .setTransactionFinishedCallback(this) // set transaction finish callback (sdk callback)
                .setMerchantBaseUrl(base_url) //set merchant url
                .enableLog(true) // enable sdk log
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // will replace theme on snap theme on MAP
                .buildSDK();
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {

        if (result.getResponse() != null) {
            switch (result.getStatus()) {
                case TransactionResult.STATUS_SUCCESS:
                    Toast.makeText(this, "Transaction Finished. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_PENDING:
                    Toast.makeText(this, "Transaction Pending. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed. ID: " + result.getResponse().getTransactionId() + ". Message: " + result.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
            result.getResponse().getValidationMessages();
        } else if (result.isTransactionCanceled()) {
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show();
        } else {
            if (result.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
            }
        }

        apiService.afterTransaction(
                Integer.parseInt(kosanTransactionData.getTransactionDetails().getOrderId()),
                result.getResponse().getTransactionId(),
                Integer.parseInt(sharedPreferenceManager.getAppUserId()),
                result.getStatus(),
                result.getResponse().getPaymentType()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: " + response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainActivityIntent);
        finish();
    }

    private TransactionRequest initTransactionRequest(KosanTransactionData transaction) {
        // Create new Transaction Request
        TransactionRequest transactionRequestNew = new
                TransactionRequest(System.currentTimeMillis() + "", Double.valueOf(transaction.getTransactionDetails().getGrossAmount()));

        //set customer details
        transactionRequestNew.setCustomerDetails(initCustomerDetails());

        // Add item details into item detail list.
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(transaction.getItemDetails());

        transactionRequestNew.setItemDetails(itemDetailsArrayList);


        return transactionRequestNew;
    }

    private retrofit2 transactionRequestNew = new itemDetailsArrayList(){
        transactionRequestNew
    }


    private CustomerDetails initCustomerDetails() {

        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(getApplicationContext());

        //define customer detail (mandatory for coreflow)
        CustomerDetails mCustomerDetails = new CustomerDetails();
        mCustomerDetails.setFirstName(sharedPreferenceManager.getAppFirstName());
        mCustomerDetails.setLastName(sharedPreferenceManager.getAppLastName());
        mCustomerDetails.setEmail(sharedPreferenceManager.getAppEmail());
        return mCustomerDetails;
    }
}
