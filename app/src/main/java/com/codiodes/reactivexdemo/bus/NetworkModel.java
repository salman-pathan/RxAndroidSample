package com.codiodes.reactivexdemo.bus;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by salmankhan on 12/02/16.
 */
public class NetworkModel {

    private static NetworkModel networkModel;
    private NetworkModel() {}

    public static NetworkModel getInstance() {
        if (networkModel == null)
            networkModel = new NetworkModel();
        return networkModel;
    }

    public Observable<String> getData() {
        Observable<String> fetchGoogle = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String data = null;
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://www.google.com")
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    data = response.body().string();
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
                subscriber.onNext(data);
            }
        });
        return fetchGoogle;
    }

}
