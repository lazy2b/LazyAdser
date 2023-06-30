package com.lazylibs.adsplasher;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class SplashAdvertHolder {
    View root;
    ImageView iv_advert;
    TextView tv_countdown;
    CountDownTimer timer;
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

        timer = new CountDownTimer(intervalCount*1000l, 1000l) {
            int count = intervalCount;
            @Override
            public void onTick(long millisUntilFinished) {
                update(--count);
            }

            @Override
            public void onFinish() {
                update(0);
                showNext();
            }
        };
        tv_countdown.setOnClickListener(v -> {
            if (timer != null) {
                timer.cancel();
            }
            showNext();
        });
        return this;
    }
}