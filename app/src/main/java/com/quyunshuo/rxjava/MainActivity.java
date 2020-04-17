package com.quyunshuo.rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.quyunshuo.rxjava.databinding.ActivityMainBinding;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Author: QuYunShuo
 * @Time: 2020/4/17
 * @Class: MainActivity
 * @Remark:
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {
        RxJavaCreateOperator rxJavaCreateOperator = new RxJavaCreateOperator();
        // rxJavaCreateOperator.onCreate();
        // rxJavaCreateOperator.onJust();
        // rxJavaCreateOperator.onFromArray();
        // rxJavaCreateOperator.onFromCallable();
        // rxJavaCreateOperator.onFromFuture();
        // rxJavaCreateOperator.onFromIterable();
        // rxJavaCreateOperator.onDefer();
        // rxJavaCreateOperator.onTimer();
        // rxJavaCreateOperator.onInterval();
        // rxJavaCreateOperator.onIntervalRange();
        // rxJavaCreateOperator.onRange();
        // rxJavaCreateOperator.onRangeLong();
        // rxJavaCreateOperator.onEmpty();
        rxJavaCreateOperator.onNever();
        rxJavaCreateOperator.onError();
    }
}