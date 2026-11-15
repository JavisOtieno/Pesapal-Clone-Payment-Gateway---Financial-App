package com.javy.pesapalclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.ResponseBody;

public class IndividualDealActivity extends AppCompatActivity implements OnSuccessListener {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView productNameTextView;
    private TextView accountNameTextView;
    private TextView amountTextView;
    private TextView statusTextView;
    private TextView dateTextView;
    private TextView timeTextView;
    private Button editDealButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_individual_deal);
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
        actionBar.setTitle("Deal");

        productNameTextView = (TextView) findViewById(R.id.productNameTextView);
        accountNameTextView = (TextView) findViewById(R.id.accountNameTextView);
        amountTextView = (TextView) findViewById(R.id.amountTextView);
        statusTextView = (TextView) findViewById(R.id.descriptionTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
//        editDealButton = (Button) findViewById(R.id.editDealButton);

        String dealId = getIntent().getStringExtra("dealId");

//        editDealButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent(IndividualDealActivity.this,
////                        EditDealActivity.class);
////                intent.putExtra("dealId",dealId);
////                startActivity(intent);
//            }
//
//        });



        NetworkUtils.
                fetchDataGet("deal/"+dealId,
                        "", IndividualDealActivity.this,
                        IndividualDealActivity.this);
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

            if (result != null && result.contains("deal")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("deal") &&
                        jsonObject.get("deal") instanceof JSONObject
                ) {

                    JSONObject deal = jsonObject.getJSONObject("deal");

                    productNameTextView.setText(deal.getJSONObject("product").
                            getString("name"));
                    accountNameTextView.setText(deal.getJSONObject("account")
                            .getString("name"));
                    amountTextView.setText(deal.getString("amount"));
                    String status = deal.getString("status");
                    status = status.substring(0, 1).toUpperCase() +
                            status.substring(1).toLowerCase();
                    statusTextView.setText(status);

                    String createdAt = deal.getString("created_at");
                    ZonedDateTime dateTime = Instant.parse(createdAt).atZone(ZoneId.of("Africa/Nairobi"));
                    String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
                    String formattedTime = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                    dateTextView.setText(formattedDate);
                    timeTextView.setText(formattedTime);


                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(IndividualDealActivity.this,message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toasts.toastIconError(IndividualDealActivity.this,"Error Loading Account. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

                }

            }else{

                Toasts.toastIconError(IndividualDealActivity.this,"Loading Account Failed. Please Try Again");
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
}