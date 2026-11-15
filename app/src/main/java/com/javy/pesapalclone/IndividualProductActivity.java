package com.javy.pesapalclone;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import okhttp3.ResponseBody;

public class IndividualProductActivity extends AppCompatActivity implements OnSuccessListener{
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView productNameTextView;
    private TextView descriptionTextView;
    private TextView accountNameTextView;
    private TextView amountSoldTextView;
    private ImageView productImageView;
    private String mLatitude;
    private String mLongitude;
    private Button editProductButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_individual_product);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Product");

        productNameTextView = (TextView) findViewById(R.id.productNameTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        accountNameTextView = (TextView) findViewById(R.id.targetAmountTextView);
        amountSoldTextView = (TextView) findViewById(R.id.amountSoldTextView);

        String productId = getIntent().getStringExtra("productId");

        productImageView = findViewById(R.id.productImageView);



        NetworkUtils.
                fetchDataGet("product/"+productId,
                        "", IndividualProductActivity.this,
                        IndividualProductActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSuccess(ResponseBody responseBody) {
        String responseBodyString = null;
        try {
            String result = responseBody.string();
            System.out.println("Result: "+result);

            if (result != null && result.contains("product")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("product") &&
                        jsonObject.get("product") instanceof JSONObject
                ) {

                    JSONObject product = jsonObject.getJSONObject("product");

                    productNameTextView.setText(product.getString("name"));
                    descriptionTextView.setText(product.getString("description"));

                    String targetAmount = product.getString("target_amount");
                    String targetType = product.getString("target_type");

                    amountSoldTextView.setText(formatAmount(targetType,
                            product.getString("deals_sum_amount")));


                    accountNameTextView.setText(formatAmount(targetType,
                            targetAmount));



                    System.out.println("Image Url: "+Constants.STORAGE_URL+product.getString("image"));

                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(IndividualProductActivity.this,message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toasts.toastIconError(IndividualProductActivity.this,"Error Loading Account. Please Try Again");//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

                }

            }else{

                Toasts.toastIconError(IndividualProductActivity.this,"Loading Account Failed. Please Try Again");
//                FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

            }



//            Log.d("API Success", "Data: " + jsonObject.toString());


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onComplete() {

    }
    public String formatAmount(String targetType, String formatAmount) {

        double amount = Double.parseDouble(formatAmount);
        String formattedAmount = NumberFormat.getNumberInstance(Locale.US).format(amount);

        // Add prefix or suffix based on targetType
        if ("value".equalsIgnoreCase(targetType)) {
            formattedAmount = "KSh. " + formattedAmount;
        } else if ("quantity".equalsIgnoreCase(targetType)) {
            formattedAmount = formattedAmount + " units";
        }
        return formattedAmount;
    }


}
