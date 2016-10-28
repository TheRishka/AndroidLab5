package com.therishka.androidlab5;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import static com.therishka.androidlab5.LoginActivity.ERROR_LOGIN;
import static com.therishka.androidlab5.LoginActivity.RANDOM_LOGIN;

@SuppressWarnings("WeakerAccess")
public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private AsyncCallback mCallback;
    private final String mEmail;
    private final String mPassword;

    UserLoginTask(String email, String password, @NonNull AsyncCallback callback) {
        mEmail = email;
        mPassword = password;
        mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        if (mCallback != null) {
            mCallback.setProgress(true);
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            // Simulate some heavy work.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return false;
        }
        if (mEmail.equals(ERROR_LOGIN[0]) || mPassword.equals(ERROR_LOGIN[1])) {
            mCallback.resultError();
        }

        return RANDOM_LOGIN[0].equals(mEmail) && RANDOM_LOGIN[1].equals(mPassword);
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (mCallback != null) {
            mCallback.setProgress(false);

            if (success) {
                mCallback.resultSuccess();
            } else {
                mCallback.resultError();
            }
        }
    }

    @Override
    protected void onCancelled() {
        if (mCallback != null) {
            mCallback.taskCancelled();
            mCallback.setProgress(false);
        }
    }
}