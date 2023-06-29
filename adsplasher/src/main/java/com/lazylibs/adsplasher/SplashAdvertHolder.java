package com.lazylibs.adsplasher;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class SplashAdvertHolder {
    View root;
    ImageView iv_advert;
    TextView tv_countdown;
    DisposableSubscriber countDownDisposable;
    ISplashAdvertHandler iHandler;

    public SplashAdvertHolder(View root, ISplashAdvertHandler handler) {
        this.root = root;
        this.iHandler = handler;
        iv_advert = this.root.findViewById(R.id.iv_advert);
        tv_countdown = this.root.findViewById(R.id.countdown_tv);
    }

    public interface ISplashAdvertHandler {
        void showNext(View root);
    }

    void showNext() {
        if (iHandler != null) {
            root.post(() -> {
                tv_countdown.setVisibility(View.INVISIBLE);
                iHandler.showNext(root);
            });
        }
    }

    void update(Object count) {
        root.post(() -> {
            if (tv_countdown.getVisibility() != View.VISIBLE) {
                tv_countdown.setVisibility(View.VISIBLE);
            }
            tv_countdown.setText(String.format("跳过%ss", String.valueOf(count)));
        });
    }

    public SplashAdvertHolder show(AdvertModel advert) {
        Glide.with(root)
                .load(advert.imageUrl)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(iv_advert);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNext();
//                H5.clickAdvert(v.getContext(), advert);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                Uri content_url = Uri.parse(advert.href);
//                intent.setData(content_url);
//                v.getContext().startActivity(intent);
            }
        });
        final int intervalCount = advert.interval;
        countDownDisposable = Flowable.intervalRange(1, intervalCount, 0, 1, TimeUnit.SECONDS)
                .observeOn(Schedulers.computation())
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(aLong -> String.valueOf(intervalCount - aLong))
                .subscribeWith(new DisposableSubscriber<String>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
//                        update(intervalCount);
                    }

                    @Override
                    public void onNext(String count) {
                        update(count);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showNext();
                    }

                    @Override
                    public void onComplete() {
                        update(0);
                        showNext();
                    }
                });
        tv_countdown.setOnClickListener(v -> {
            if (countDownDisposable != null && !countDownDisposable.isDisposed()) {
                countDownDisposable.dispose();
            }
            showNext();
        });
        return this;
    }
}