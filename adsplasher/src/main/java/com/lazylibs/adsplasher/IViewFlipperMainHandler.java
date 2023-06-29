package com.lazylibs.adsplasher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ViewFlipper;

public interface IViewFlipperMainHandler {
//    void showMainContainer(View root);

    void initMain();

    Window getWindowToken();

    AdvertModel getCacheAdvert();

    View getMainContainer(ViewFlipper viewFlipper);

    default View getAdvertContainer(ViewFlipper viewFlipper) {
        return LayoutInflater.from(viewFlipper.getContext()).inflate(R.layout.tpl_splash_advert, null);
    }

}