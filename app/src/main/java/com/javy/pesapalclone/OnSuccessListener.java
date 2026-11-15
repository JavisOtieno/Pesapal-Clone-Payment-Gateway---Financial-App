package com.javy.pesapalclone;

import okhttp3.ResponseBody;

public interface OnSuccessListener {
    void onSuccess(ResponseBody responseBody);
    void onComplete();

}

