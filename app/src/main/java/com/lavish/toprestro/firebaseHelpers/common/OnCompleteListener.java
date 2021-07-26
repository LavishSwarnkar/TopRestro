package com.lavish.toprestro.firebaseHelpers.common;

public interface OnCompleteListener<T> {

    void onResult(T t);
    void onError(String e);

}
