package com.codiodes.reactivexdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.codiodes.reactivexdemo.bus.RxBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.subtitle)
    TextView mSubtitle;

    RxBus mRxBus;
    private CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mRxBus = RxBus.getRxBusSingleton();
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, new BlankFragment())
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSubscriptions = new CompositeSubscription();
        mSubscriptions.add(mRxBus.toObserverable()
        .subscribe(new Action1<Object>() {
            @Override
            public void call(Object data) {
                if (data instanceof String) {
                    mSubtitle.setText((String) data);
                }
            }
        }));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSubscriptions.unsubscribe();
    }
}
