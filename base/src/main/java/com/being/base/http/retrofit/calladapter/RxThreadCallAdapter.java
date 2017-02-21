package com.being.base.http.retrofit.calladapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by zhangpeng on 17/2/21.
 */
@SuppressWarnings("unchecked")
public class RxThreadCallAdapter extends CallAdapter.Factory {
    private RxJavaCallAdapterFactory rxFactory = RxJavaCallAdapterFactory.create();
    private Scheduler subscribeScheduler;
    private Scheduler observerScheduler;

    public RxThreadCallAdapter(Scheduler subscribeScheduler, Scheduler observerScheduler) {
        this.subscribeScheduler = subscribeScheduler;
        this.observerScheduler = observerScheduler;
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        CallAdapter<Observable<?>> callAdapter = (CallAdapter<Observable<?>>) rxFactory.get(returnType, annotations, retrofit);
        return callAdapter != null ? new ThreadCallAdapter(callAdapter) : null;
    }

    private final class ThreadCallAdapter implements CallAdapter<Observable<?>> {
        CallAdapter<Observable<?>> delegateAdapter;

        ThreadCallAdapter(CallAdapter<Observable<?>> delegateAdapter) {
            this.delegateAdapter = delegateAdapter;
        }

        @Override
        public Type responseType() {
            return delegateAdapter.responseType();
        }

        @Override
        public <T> Observable<?> adapt(Call<T> call) {
            return delegateAdapter.adapt(call)
                    .subscribeOn(subscribeScheduler)
                    .observeOn(observerScheduler);
        }
    }
}