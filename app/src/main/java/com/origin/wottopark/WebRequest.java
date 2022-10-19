package com.origin.wottopark;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

public class WebRequest {
    public static AsyncHttpClient client;

    static {
        //create object of loopj client
        //443 will save you from ssl exception
            client = new AsyncHttpClient(true, 80, 443);


    }

    //concatenation of base url and file name
//    private static String getAbsoluteUrl(String relativeUrl) {
//        Log.d("response URL: ", GlobleClass.BASE_URL + relativeUrl+" ");
//        return GlobleClass.BASE_URL + relativeUrl;
//    }
    public static void post(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        client.post(context, url,params, responseHandler);
        Log.d("response", "post: request sent");
    }

}
