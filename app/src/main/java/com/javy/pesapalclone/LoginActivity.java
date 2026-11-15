package com.javy.pesapalclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.javy.pesapalclone.utils.EncryptedPrefsUtil;
import com.javy.pesapalclone.utils.Tools;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity implements OnSuccessListener {

    private View parent_view;
    private Button signinButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        parent_view = findViewById(android.R.id.content);
        signinButton = (Button) findViewById(R.id.signinButton);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();


                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("email", email);
                    requestBody.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                NetworkUtils.
                        fetchDataPost("login",
                                "",requestBody,LoginActivity.this,
                                LoginActivity.this);
            }
        });

        ((View) findViewById(R.id.sign_up_for_account)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(parent_view, "Sign up for an account", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSuccess(ResponseBody responseBody) {
//        Toasts.toastIconError(LoginActivity.this,"Logged in Successfully");


        try {

            String result = responseBody.string();
            System.out.println("Result: "+result);

            JSONObject jsonObject = null;

            jsonObject = new JSONObject(result);


        if (
                jsonObject.has("message") &&
                        jsonObject.get("message") instanceof String &&
                        jsonObject.has("status") &&
                        jsonObject.get("status") instanceof String
        ) {
            String message = jsonObject.getString("message");
            String status = jsonObject.getString("status");

            if (status.equals("success")) {
                //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                EncryptedPrefsUtil.saveString("authToken", jsonObject.getString("authToken"));
                EncryptedPrefsUtil.saveString("userId", jsonObject.getString("userId"));
                EncryptedPrefsUtil.saveString("email", email);
                EncryptedPrefsUtil.saveString("password",password);

                long currentTimeMillis = System.currentTimeMillis();
                long expiryMillis = Constants.EXPIRY_MILLIS+currentTimeMillis;

                EncryptedPrefsUtil.saveString("tokenExpiry", expiryMillis+"");




                startActivity(intent);

            } else if(status.equals("failed")) {
                Toasts.toastIconError(LoginActivity.this, message);

            } else{
                Toasts.toastIconError(LoginActivity.this,
                        "Login Failed! Please Try Again");
//                FirebaseCrashlytics.getInstance().recordException(new Exception("Login  Error: "+result));
            }
        }else{
            Toasts.toastIconError(LoginActivity.this,
                    "Login Failed! Please Try Again");
//            FirebaseCrashlytics.getInstance().recordException(new Exception("Login Error JSON Object: "+result));

        }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onComplete() {

    }
}
