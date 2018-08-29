package id.notation.kosanku.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import id.notation.kosanku.MainActivity;
import id.notation.kosanku.R;
import id.notation.kosanku.kamarkos.KamarKosScanQRActivity;
import id.notation.kosanku.utils.SharedPreferenceManager;
import id.notation.kosanku.utils.api.ApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDashboardFragment extends Fragment {

    ImageView qrView;
    FloatingActionButton qrScanFAB;
    SharedPreferenceManager sharedPreferenceManager;
    ApiService apiService;

    public UserDashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_dashboard, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("User Dashboard");

        sharedPreferenceManager = new SharedPreferenceManager(getContext());
        qrView = view.findViewById(R.id.qrView);
        qrScanFAB = view.findViewById(R.id.transactionScan);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(sharedPreferenceManager.getAppUserId(), BarcodeFormat.QR_CODE,400,400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        qrScanFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), KamarKosScanQRActivity.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
