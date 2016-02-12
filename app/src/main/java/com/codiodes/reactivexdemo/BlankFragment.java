package com.codiodes.reactivexdemo;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codiodes.reactivexdemo.bus.NetworkModel;
import com.codiodes.reactivexdemo.bus.RxBus;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    @Bind(R.id.btn_fetch)
    Button mBtnFetch;
    private RxBus mRxBus;

    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_blank, container, false);
        ButterKnife.bind(this, layout);
        mRxBus = RxBus.getRxBusSingleton();
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @OnClick(R.id.btn_fetch)
    public void onClickFetch() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        NetworkModel.getInstance().getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String data) {
                        pDialog.cancel();
                        mRxBus.send(data);
                    }
                });
    }

    private String netWorkCall() {
        String data = null;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://www.google.com")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            data = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

}
