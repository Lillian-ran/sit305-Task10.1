package com.example.personalizedlearningexperienceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.tasks.Task;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class UpgradeActivity extends AppCompatActivity {

    private PaymentsClient paymentsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        paymentsClient = Wallet.getPaymentsClient(
                this,
                new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build()
        );
        Wallet.WalletOptions walletOptions =
                new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_TEST) // 使用测试环境
                        .build();


        Button purchaseBasicBtn = findViewById(R.id.btnStarter);
        Button purchaseProBtn = findViewById(R.id.btnIntermediate);
        Button purchasePremiumBtn = findViewById(R.id.btnAdvanced);

        purchaseBasicBtn.setOnClickListener(view -> requestPayment("4.99"));
        purchaseProBtn.setOnClickListener(view -> requestPayment("9.99"));
        purchasePremiumBtn.setOnClickListener(view -> requestPayment("14.99"));
    }

    private void requestPayment(String price) {
        JSONObject paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(price);
        if (paymentDataRequestJson == null) return;

        PaymentDataRequest request =
                PaymentDataRequest.fromJson(paymentDataRequestJson.toString());

        AutoResolveHelper.resolveTask(
                paymentsClient.loadPaymentData(request), this, 991);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 991) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    PaymentData paymentData = PaymentData.getFromIntent(data);
                    String json = paymentData.toJson();
                    Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show();
                    break;
                case Activity.RESULT_CANCELED:
                    break;
                case AutoResolveHelper.RESULT_ERROR:
                    Toast.makeText(this, "Payment Failed", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
