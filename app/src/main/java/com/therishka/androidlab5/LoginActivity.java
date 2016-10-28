package com.therishka.androidlab5;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements AsyncCallback {

    public static final String[] RANDOM_LOGIN = new String[]{
            "TheRishka", "HIBRO"
    };

    private UserLoginTask mAuthTask = null;

    private EditText mLoginInput;
    private EditText mPasswordInput;
    private View mProgressView;
    private Button mLoginButton;

    private View mPasswordContainer;
    private View mLoginContainer;
    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mRootView = findViewById(R.id.root_view);
        mLoginInput = (EditText) findViewById(R.id.login);

        mLoginContainer = findViewById(R.id.login_container);
        mPasswordContainer = findViewById(R.id.password_container);

        mPasswordInput = (EditText) findViewById(R.id.password);


        mLoginButton = (Button) findViewById(R.id.email_sign_in_button);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });

        mProgressView = findViewById(R.id.login_progress);
    }

    private void doLogin() {
        if (mAuthTask != null) {
            return;
        }
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
            mLoginInput.setError(getString(R.string.error_invalid_email));
            focusView = mLoginInput;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mAuthTask = new UserLoginTask(email, password, this);
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
        mAuthTask = null;
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_anim);
        anim.reset();
        mLoginButton.clearAnimation();
        mLoginButton.startAnimation(anim);
    }

    @Override
    public void resultSuccess() {
        mAuthTask = null;
        Snackbar.make(mRootView, "SUCCESS LOGIN", Snackbar.LENGTH_SHORT).show();
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
}

