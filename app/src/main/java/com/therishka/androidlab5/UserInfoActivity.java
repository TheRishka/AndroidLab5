package com.therishka.androidlab5;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * @author Rishad Mustafaev
 */

public class UserInfoActivity extends ToolbarActivity {

    public static final int CHANGE_USER_DATA_REQUEST_CODE = 123;

    public static final String USER_NAME_KEY = "user_name";
    public static final String USER_SURNAME_KEY = "user_surname";
    public static final String USER_AGE_KEY = "user_age";
    public static final String USER_SEX_KEY = "user_sex";

    TextView mUserName;
    TextView mUserSurname;
    TextView mUserAge;
    TextView mUserSex;

    View mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.user_home);
        super.onCreate(savedInstanceState);
        mRoot = findViewById(R.id.root_view);
        mUserName = (TextView) findViewById(R.id.user_data_name);
        mUserSurname = (TextView) findViewById(R.id.user_data_surname);
        mUserAge = (TextView) findViewById(R.id.user_data_age);
        mUserSex = (TextView) findViewById(R.id.user_data_sex);
        if (savedInstanceState != null) {
            logMessage("onCreate with savedInstanceState");
            restoreDataFromState(savedInstanceState);
        } else {
            logMessage("onCreate without savedInstanceState");
        }
    }

    private void restoreDataFromState(@NonNull Bundle savedState) {
        mUserName.setText(savedState.getString(USER_NAME_KEY, ""));
        mUserSurname.setText(savedState.getString(USER_SURNAME_KEY, ""));
        mUserAge.setText(savedState.getString(USER_AGE_KEY, ""));
        mUserSex.setText(savedState.getString(USER_SEX_KEY, ""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        logMessage("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logMessage("onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logMessage("onSaveInstanceState");
        outState.putString(USER_NAME_KEY, mUserName.getText().toString());
        outState.putString(USER_SURNAME_KEY, mUserSurname.getText().toString());
        outState.putString(USER_AGE_KEY, mUserAge.getText().toString());
        outState.putString(USER_SEX_KEY, mUserSex.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        logMessage("onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        logMessage("onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    public void changeUserDataBtnClick(View v) {
        startChangeUserDataActivity();
    }

    private void startChangeUserDataActivity() {
        Intent intent = new Intent(this, ChangeUserDataActivity.class);
        startActivityForResult(intent, CHANGE_USER_DATA_REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @StringRes
    @Override
    public int getTitleTextResId() {
        return R.string.user_activity_title;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void setupAdditionalToolbarSettings() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE_USER_DATA_REQUEST_CODE) {
            if (resultCode == ChangeUserDataActivity.RESULT_CHANGED) {
                if (data.getExtras() != null) {
                    Bundle extra = data.getExtras();
                    mUserName.setText(extra.getString(USER_NAME_KEY, ""));
                    mUserSurname.setText(extra.getString(USER_SURNAME_KEY, ""));
                    mUserAge.setText(extra.getString(USER_AGE_KEY, ""));
                    mUserSex.setText(extra.getString(USER_SEX_KEY, ""));
                }
            } else {
                Snackbar.make(mRoot, R.string.snackbar_action_canceled, Snackbar.LENGTH_SHORT).show();
            }
        } else {
            Log.d(this.getClass().getName(), "Not CHANGE_USER_DATA_REQUEST_CODE");
        }
    }

    private void logMessage(String text) {
        Log.d(this.getClass().getName(), text.toUpperCase());
    }
}
