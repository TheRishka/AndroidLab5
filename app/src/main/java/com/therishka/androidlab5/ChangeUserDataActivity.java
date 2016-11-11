package com.therishka.androidlab5;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

public class ChangeUserDataActivity extends ToolbarActivity implements RadioGroup.OnCheckedChangeListener, AsyncCallback {

    public static final int RESULT_CANCEL = 101;
    public static final int RESULT_CHANGED = 102;

    public static final String PROGRESS_VALUE_KEY = "progress_value";

    @StringRes
    private int USER_SEX_STRING_TEXT = R.string.user_male_full;

    private EditText userInputName;
    private EditText userInputSurname;
    private EditText userInputAge;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_user_data);
        super.onCreate(savedInstanceState);

        userInputName = (EditText) findViewById(R.id.user_name_input);
        userInputSurname = (EditText) findViewById(R.id.user_surname_input);
        userInputAge = (EditText) findViewById(R.id.user_age_input);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        RadioGroup userInputSexGroup = (RadioGroup) findViewById(R.id.user_sex_input);
        userInputSexGroup.setOnCheckedChangeListener(this);
        if (savedInstanceState != null) {
            logMessage("onCreate with savedState");
            mProgressBar.setProgress(savedInstanceState.getInt(PROGRESS_VALUE_KEY));
            if(getTaskHolderFragment().isTaskActive()){
                mProgressBar.setVisibility(View.VISIBLE);
            }
        } else {
            logMessage("onCreate without state");
        }
    }

    private ChangeUserDataTaskHolderFragment getTaskHolderFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        ChangeUserDataTaskHolderFragment fragment =
                (ChangeUserDataTaskHolderFragment) fragmentManager.
                        findFragmentByTag(ChangeUserDataTaskHolderFragment.class.getName());

        if (fragment == null) {
            fragment = new ChangeUserDataTaskHolderFragment();
            fragmentManager.
                    beginTransaction().
                    add(fragment, ChangeUserDataTaskHolderFragment.class.getName()).
                    commit();
        }
        return fragment;
    }

    @Override
    public void setProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void resultError() {
        setProgress(false);
    }

    @Override
    public void resultSuccess() {
        setProgress(false);
        closeThisActivity(true);
    }

    @Override
    public void taskCancelled() {
        setProgress(false);
    }

    @Override
    public void onProgressUpdate(int value) {
        mProgressBar.setProgress(value);
    }

    @Override
    protected void onResume() {
        logMessage("onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        logMessage("onPause");
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        logMessage("onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        logMessage("onSaveInstanceState");
        outState.putInt(PROGRESS_VALUE_KEY, mProgressBar.getProgress());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        logMessage("onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        closeThisActivity(false);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.user_sex_male:
                USER_SEX_STRING_TEXT = R.string.user_male_full;
                break;
            case R.id.user_sex_female:
                USER_SEX_STRING_TEXT = R.string.user_female_full;
                break;
        }
    }

    public void confirmBtnPressed(View v) {
        mProgressBar.setVisibility(View.VISIBLE);
        getTaskHolderFragment().startTask();
//        closeThisActivity(true);
    }

    public void cancelBtnPressed(View v) {
        if (getTaskHolderFragment().isTaskActive()) {
            getTaskHolderFragment().cancelTask();
        }
        closeThisActivity(false);
    }

    private void closeThisActivity(boolean dataChanged) {
        if (dataChanged) {
            Intent intent = new Intent();
            intent.putExtra(UserInfoActivity.USER_NAME_KEY, userInputName.getText().toString());
            intent.putExtra(UserInfoActivity.USER_SURNAME_KEY, userInputSurname.getText().toString());
            intent.putExtra(UserInfoActivity.USER_AGE_KEY, userInputAge.getText().toString());
            intent.putExtra(UserInfoActivity.USER_SEX_KEY, getString(USER_SEX_STRING_TEXT));
            setResult(RESULT_CHANGED, intent);
        } else {
            setResult(RESULT_CANCEL);
        }
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    @StringRes
    public int getTitleTextResId() {
        return R.string.user_data_change_activity_title;
    }

    @Override
    protected void setupAdditionalToolbarSettings() {
        // do nothing
    }


    private void logMessage(String text) {
        Log.d(this.getClass().getName(), text.toUpperCase());
    }
}
