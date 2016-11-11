package com.therishka.androidlab5;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

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
            logMessage("onPreExecute");
            mCallback.setProgress(true);
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        logMessage("doInBackground");
        try {
            // Simulate some heavy work.
            Thread.sleep(2000);
            logMessage("doInBackground finish");
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
            logMessage("OnPostExecute success = " + success);
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
            logMessage("On Cancelled");
//            mCallback.taskCancelled();
//            mCallback.setProgress(false);
        }
    }

    private void logMessage(String text) {
        Log.d(this.getClass().getName(), text);
    }
}