package com.reelyactive.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by saiimons on 16-03-03.
 */
public class ContextApi {
    private static final ContextInterface ctxIf;

    static {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.hyperlocalcontext.com")
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        ctxIf = retrofit.create(ContextInterface.class);
    }

    public static ContextInterface get() {
        return ctxIf;
    }
}
