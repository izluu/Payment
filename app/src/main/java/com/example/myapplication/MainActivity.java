package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Api.CreateOrder;

import org.json.JSONObject;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class MainActivity extends AppCompatActivity {
    private Button btnOK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        StrictMode.ThreadPolicy policy = new
        StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        int total = 10000;
        btnOK = findViewById(R.id.OK);

        btnOK.setOnClickListener(v -> {
            CreateOrder orderApi = new CreateOrder();
            try {
                JSONObject data = orderApi.createOrder(String.valueOf(total));
                Log.d("Amount", String.valueOf(total));

                String code = data.getString("return_code");
                Toast.makeText(getApplicationContext(), "return_code: " + code, Toast.LENGTH_LONG).show();

                if (code.equals("1")) {
                    String token = data.getString("zp_trans_token");
                    ZaloPaySDK.getInstance().payOrder(MainActivity.this, token, "demozpdk://app", new PayOrderListener() {
                        @Override
                        public void onPaymentSucceeded(String s, String s1, String s2) {
                            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onPaymentCanceled(String s, String s1) {
                            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }catch(Exception e){
                e.printStackTrace();
            }


    });
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}