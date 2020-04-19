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
        // 创建操作符
        // RxJavaCreateOperator rxJavaCreateOperator = new RxJavaCreateOperator();
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
        // rxJavaCreateOperator.onNever();
        // rxJavaCreateOperator.onError();

        // 转换操作符
        // RxJavaTransformOperator rxJavaTransformOperator = new RxJavaTransformOperator();
        // rxJavaTransformOperator.onMap();
        // rxJavaTransformOperator.onFlatMap();
        // rxJavaTransformOperator.onConcatMap();
        // rxJavaTransformOperator.onBuffer();
        // rxJavaTransformOperator.onGroupBy();
        // rxJavaTransformOperator.onScan();
        // rxJavaTransformOperator.onWindow();

        // 组合操作符
        // RxJavaCombinationOperator rxJavaCombinationOperator = new RxJavaCombinationOperator();
        // rxJavaCombinationOperator.onConcat();
        // rxJavaCombinationOperator.onConcatArray();
        // rxJavaCombinationOperator.onMergeAndMergeArray();
        // rxJavaCombinationOperator.concatArrayDelayErrorAndMergeArrayDelayError();
        // rxJavaCombinationOperator.onZip();
        // rxJavaCombinationOperator.onCombineLatestAndCombineLatestDelayError();
        // rxJavaCombinationOperator.onReduce();
        // rxJavaCombinationOperator.onCollect();
        // rxJavaCombinationOperator.onStartWithAndStartWithArray();
        // rxJavaCombinationOperator.onCount();

        // 功能操作符
        // RxJavaFunctionalOperator rxJavaFunctionalOperator = new RxJavaFunctionalOperator();
        // rxJavaFunctionalOperator.onDelay();
        // rxJavaFunctionalOperator.onDoOnEach();
        // rxJavaFunctionalOperator.onDoOnNext();
        // rxJavaFunctionalOperator.onDoAfterNext();
        // rxJavaFunctionalOperator.onDoOnComplete();
        // rxJavaFunctionalOperator.onDoOnError();
        // rxJavaFunctionalOperator.onDoOnSubscribe();
        // rxJavaFunctionalOperator.onDoOnDispose();
        // rxJavaFunctionalOperator.onDoOnLifecycle();
        // rxJavaFunctionalOperator.onDoOnTerminateAndDoAfterTerminate();
        // rxJavaFunctionalOperator.onDoFinally();
        // rxJavaFunctionalOperator.onOnErrorReturn();
        // rxJavaFunctionalOperator.onOnErrorResumeNext();
        // rxJavaFunctionalOperator.onOnExceptionResumeNext();
        // rxJavaFunctionalOperator.onRetry();
        // rxJavaFunctionalOperator.onRetryUntil();
        // rxJavaFunctionalOperator.onRetryWhen();
        // rxJavaFunctionalOperator.onRepeat();
        // rxJavaFunctionalOperator.onRepeatWhen();
        // rxJavaFunctionalOperator.onSubscribeOn();
        // rxJavaFunctionalOperator.onObserveOn();

        // 过滤操作符
        // RxJavaFilterOperator rxJavaFilterOperator = new RxJavaFilterOperator();
        // rxJavaFilterOperator.onFilter();
        // rxJavaFilterOperator.onOfType();
        // rxJavaFilterOperator.onSkip();
        // rxJavaFilterOperator.onSkipLast();
        // rxJavaFilterOperator.onDistinct();
        // rxJavaFilterOperator.onDistinctUntilChanged();
        // rxJavaFilterOperator.onTake();
        // rxJavaFilterOperator.onDebounce();
        // rxJavaFilterOperator.onFirstElementAndLastElement();
        // rxJavaFilterOperator.onElementAtAndElementAtOrError();

        // 条件操作符
        // RxJavaConditionalOperator rxJavaConditionalOperator = new RxJavaConditionalOperator();
        // rxJavaConditionalOperator.onAll();
        // rxJavaConditionalOperator.onTakeWhile();
        // rxJavaConditionalOperator.onSkipWhile();
        // rxJavaConditionalOperator.onTakeUntil();
        // rxJavaConditionalOperator.onSkipUntil();
        // rxJavaConditionalOperator.onSequenceEqual();
        // rxJavaConditionalOperator.onContains();
        // rxJavaConditionalOperator.onIsEmpty();
        // rxJavaConditionalOperator.onAmb();
        // rxJavaConditionalOperator.onDefaultIfEmpty();
    }
}