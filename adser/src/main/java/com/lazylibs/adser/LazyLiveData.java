package com.lazylibs.adser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Objects;
import java.util.function.Supplier;

public class LazyLiveData<T> extends MutableLiveData<T> {
    private final Supplier<LiveData<T>> supplier;
    private LiveData<T> currentLiveData;
    public LazyLiveData(Supplier<LiveData<T>> supplier) {
        this.supplier = supplier;
    }

    @Override
    protected void onActive() {
        if (currentLiveData == null) {
            currentLiveData = Objects.requireNonNull(supplier.get(), "supplier.get() returned null");
            currentLiveData.observeForever(this::setValue);
        }
    }

    @Override
    protected void onInactive() {
        if (currentLiveData != null && !hasActiveObservers()) {
            currentLiveData.removeObserver(this::setValue);
            currentLiveData = null;
        }
    }
}
