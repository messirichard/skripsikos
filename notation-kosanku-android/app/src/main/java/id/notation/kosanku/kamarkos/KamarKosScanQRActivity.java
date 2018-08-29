package id.notation.kosanku.kamarkos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import id.notation.kosanku.PaymentActivity;
import id.notation.kosanku.R;
import id.notation.kosanku.models.KosanOrderItem;
import id.notation.kosanku.models.KosanTransaction;
import id.notation.kosanku.utils.api.ApiService;
import id.notation.kosanku.utils.api.UtilsApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class KamarKosScanQRActivity extends AppCompatActivity {

    IntentIntegrator qrScan;
    ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamar_kos_scan_qr);

        apiService = UtilsApi.getAPIService();

        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(true);
        qrScan.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "onActivityResult: " + result.getContents());
                Toast.makeText(getApplicationContext(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                getOrder(Integer.parseInt(result.getContents()));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void getOrder(int orderId)
    {
        apiService.getOrder(orderId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject data = new JSONObject(response.body().string()).getJSONObject("data");
                    Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                    intent.putExtra("transaction_data", data.toString());
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
