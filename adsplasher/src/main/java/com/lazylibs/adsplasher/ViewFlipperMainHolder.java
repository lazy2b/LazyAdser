package com.lazylibs.adsplasher;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import com.lazylibs.adsplasher.IViewFlipperMainHandler;

public class ViewFlipperMainHolder {

    private IViewFlipperMainHandler iMainHandler;
    private ViewFlipper viewFlipper;
    private View mainRoot;

    private View advertRoot;

    public boolean isMainAdded() {
        return mainViewState != MainViewState.Default;
    }

    public boolean isMainShown() {
        return mainViewState == MainViewState.isShown;
    }

    enum MainViewState {
        Default, isAdded, isShown
    }

    private MainViewState mainViewState = MainViewState.Default;

    public ViewFlipperMainHolder(@NonNull ViewFlipper vFlipper, @NonNull IViewFlipperMainHandler handler) {
        viewFlipper = vFlipper;
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(viewFlipper.getContext(),
                R.anim.push_left_in));
        Animation animOut = AnimationUtils.loadAnimation(viewFlipper.getContext(),
                R.anim.push_left_out);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mainViewState == MainViewState.isShown) {
//                    Utils.quitFullScreen(iMainHandler.getWindowToken());
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        viewFlipper.setOutAnimation(animOut);
        iMainHandler = handler;
        init();
    }

    private void init() {
        AdvertModel advert = iMainHandler.getCacheAdvert();
        if (advert != null) {
            viewFlipper.addView(advertRoot = iMainHandler.getAdvertContainer(viewFlipper));
            new SplashAdvertHolder(advertRoot, root -> showMainView()).show(advert);
        }
        viewFlipper.addView(mainRoot = iMainHandler.getMainContainer(viewFlipper));
        mainViewState = MainViewState.isAdded;
        if (advertRoot == null) {
            showMainView();
        }
    }

    private void showMainView() {
        viewFlipper.post(() -> {
            if (advertRoot != null) {
                viewFlipper.showNext();
                viewFlipper.removeView(advertRoot);
            } else {
                ScreenAdaptation.quitFullScreen(iMainHandler.getWindowToken());
            }
            mainViewState = MainViewState.isShown;
            iMainHandler.initMain();
        });
    }
}
