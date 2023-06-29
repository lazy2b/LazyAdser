package com.lazylibs.adsplasher;

import android.app.Activity;

public interface IMainWorkerFlowHandler{//} extends H5.ILoginTokenHandler {

    Activity getActivity();

    void initStaticData();

    default void initNetworkData(boolean byReload) {
        checkVersion();
        loadAdverts();
    }

    void loadAdverts();

    void checkVersion();
}