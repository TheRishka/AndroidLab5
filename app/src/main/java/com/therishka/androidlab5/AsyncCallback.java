package com.therishka.androidlab5;


@SuppressWarnings("WeakerAccess")
public interface AsyncCallback {
    void setProgress(boolean show);

    void resultError();

    void resultSuccess();

    void taskCancelled();

    void onProgressUpdate(int value);
}
