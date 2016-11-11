package com.therishka.androidlab5;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends ToolbarActivity implements AsyncCallback, OnClickListener {

    public static final String[] RANDOM_LOGIN = new String[]{
            "TheRishka", "12345"
    };
    public static final String[] ERROR_LOGIN = new String[]{"123123", "123123"};

    public static final String PREFERENCES_NAME = "android_lab_prefs";
    public static final String IS_LOGGED = "is_logged_in";

    private UserLoginTask mAuthTask = null;

    private EditText mLoginInput;
    private EditText mPasswordInput;
    private View mProgressView;
    private Button mLoginButton;
    private Button mHanderSuccessButton;
    private Button mHandlerErrorButton;

    private View mPasswordContainer;
    private View mLoginContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isLoggedInAlready()) {
            startUserInfoActivity(false);
//            finish();
        }
        logMessage("ON CREATE!");
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
        mLoginInput = (EditText) findViewById(R.id.login);

        mLoginContainer = findViewById(R.id.login_container);
        mPasswordContainer = findViewById(R.id.password_container);

        mPasswordInput = (EditText) findViewById(R.id.password);


        mLoginButton = (Button) findViewById(R.id.sign_in_button);
        mLoginButton.setOnClickListener(this);

        mHanderSuccessButton = (Button) findViewById(R.id.start_handler_with_success);
        mHanderSuccessButton.setOnClickListener(this);

        mHandlerErrorButton = (Button) findViewById(R.id.start_handler_with_error);
        mHandlerErrorButton.setOnClickListener(this);

        Button cancelBtn = (Button) findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(this);

        mProgressView = findViewById(R.id.login_progress);
    }

    private boolean isLoggedInAlready() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        return preferences.getBoolean(IS_LOGGED, false);
    }

    @Override
    protected void onPause() {
        logMessage("ON PAUSE!");
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        logMessage("ON CONFIGURATION CHANGED!");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        logMessage("ON RESUME!");
        super.onResume();
    }

    @Override
    protected int getTitleTextResId() {
        return R.string.login_activity_title;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void setupAdditionalToolbarSettings() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(null);
    }

    @Override
    public void onProgressUpdate(int value) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                doLogin();
                break;
            case R.id.start_handler_with_error:
                startErrorHandler();
                break;
            case R.id.start_handler_with_success:
                startValidHandler();
                break;
            case R.id.cancel_button:
                cancelTask();
                break;
        }
    }

    private void cancelTask() {
        if (mAuthTask != null) {
            mAuthTask.cancel(true);
        }
    }

    private void startErrorHandler() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    resultError();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void startValidHandler() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                setProgress(true);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setProgress(false);
                resultError();
            }
        });
    }

    private void doLogin() {
        mLoginInput.setError(null);
        mPasswordInput.setError(null);

        String email = mLoginInput.getText().toString();
        String password = mPasswordInput.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordInput.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordInput;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mLoginInput.setError(getString(R.string.error_field_required));
            focusView = mLoginInput;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mLoginInput.setError(getString(R.string.error_invalid_login));
            focusView = mLoginInput;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mAuthTask = new UserLoginTask(email, password, this);
            //noinspection ConfusingArgumentToVarargsMethod
            mAuthTask.execute((Void) null);
        }
    }

    @Override
    public void setProgress(boolean show) {
        mLoginContainer.setVisibility(show ? View.GONE : View.VISIBLE);
        animateAlphaForView(mLoginContainer, show);
        mPasswordContainer.setVisibility(show ? View.GONE : View.VISIBLE);
        animateAlphaForView(mPasswordContainer, show);

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        animateAlphaForView(mProgressView, !show);
    }

    @Override
    public void resultError() {
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_anim);
        anim.reset();
        mLoginButton.clearAnimation();
        mLoginButton.startAnimation(anim);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void resultSuccess() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_LOGGED, true);
        editor.commit();
        startUserInfoActivity(true);
    }

    private void startUserInfoActivity(boolean withAnim) {
        Intent intent = new Intent(this, UserInfoActivity.class);
        startActivity(intent);
        if (withAnim)
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void taskCancelled() {
        mAuthTask = null;
    }

    private boolean isEmailValid(@NonNull String email) {
        return email.length() > 5;
    }

    private boolean isPasswordValid(@NonNull String password) {
        return password.length() > 4;
    }

    private void animateAlphaForView(@NonNull final View target, final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        target.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                target.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });
    }

    private void logMessage(String text) {
        Log.d(this.getClass().getName(), text);
    }
}

