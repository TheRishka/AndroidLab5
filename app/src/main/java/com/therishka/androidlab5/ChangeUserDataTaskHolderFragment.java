package com.therishka.androidlab5;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

/**
 * @author Rishad Mustafaev.
 */

public class ChangeUserDataTaskHolderFragment extends Fragment {

    private AsyncCallback mAsyncCallback;
    private ChangeUserDataAsyncTask mAsyncTask;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AsyncCallback) {
            mAsyncCallback = (AsyncCallback) context;
        } else {
            throw new IllegalStateException("Activity that uses this fragment MUST implement AsyncCallback interface");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    public void startTask() {
        if (mAsyncTask == null) {
            mAsyncTask = new ChangeUserDataAsyncTask();
            mAsyncTask.execute();
        }
    }

    public boolean isTaskActive() {
        return mAsyncTask != null;
    }

    public void cancelTask() {
        if (mAsyncTask != null) {
            mAsyncTask.cancel(true);
            mAsyncTask = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAsyncCallback = null;
    }

    private void logMessage(String text) {
        Log.d(this.getClass().getName(), text.toUpperCase());
    }

    public class ChangeUserDataAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            logMessage("onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            logMessage("doInBackground");
            try {
                int randomProgress = 0;
                // Simulate some heavy work.
                while (randomProgress < 100 && !isCancelled()) {
                    randomProgress += 5;
                    publishProgress(randomProgress);
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAsyncCallback.resultSuccess();
            logMessage("onPostExecute");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mAsyncCallback.onProgressUpdate(values[0]);
        }
    }
}
