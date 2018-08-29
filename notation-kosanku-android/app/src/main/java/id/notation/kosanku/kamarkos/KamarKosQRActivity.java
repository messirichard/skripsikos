package id.notation.kosanku.kamarkos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import id.notation.kosanku.R;
import id.notation.kosanku.models.KosanTransaction;
import id.notation.kosanku.models.KosanTransaction;
import id.notation.kosanku.utils.api.ApiService;
import id.notation.kosanku.utils.api.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KamarKosQRActivity extends AppCompatActivity {

    ImageView qrView;
    ApiService apiService;
    private static String TAG = "##";
    int id_kamar, id_indekos;
    String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamar_kos_qr);

        Intent intent = getIntent();

        apiService = UtilsApi.getAPIService();
        qrView = findViewById(R.id.qrView);

        id_kamar = intent.getIntExtra("kamarkos.id_kamar", 0);
        id_indekos = intent.getIntExtra("kamarkos.id_indekos", 0);

        createOrder(id_indekos, id_kamar);



    }

    public void createOrder(int id_indekos, int id_kamar)
    {

        apiService.createOrder(id_indekos, id_kamar).enqueue(new Callback<KosanTransaction>() {
            @Override
            public void onResponse(Call<KosanTransaction> call, Response<KosanTransaction> response) {
                Log.d(TAG, "onResponse: " + response.body().getData().getTransactionDetails().getOrderId());
                if (response.code() == 200) {
                    orderId = response.body().getData().getTransactionDetails().getOrderId();

                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(orderId, BarcodeFormat.QR_CODE,400,400);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        qrView.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<KosanTransaction> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }
}
