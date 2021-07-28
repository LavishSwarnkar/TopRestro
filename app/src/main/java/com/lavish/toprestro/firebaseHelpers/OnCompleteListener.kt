package com.lavish.toprestro.firebaseHelpers;

public interface OnCompleteListener<T> {

    void onResult(T t);
    void onError(String e);

}
