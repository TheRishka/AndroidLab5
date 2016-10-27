package com.therishka.androidlab5;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class LoginActivity extends AppCompatActivity {

    private static final String[] RANDOM_LOGIN = new String[]{
            "TheRishka", "HIBRO"
    };

    private UserLoginTask mAuthTask = null;

    private EditText mLoginInput;
    private EditText mPasswordInput;
    private View mProgressView;
    private Button mLoginButton;

    private View mPasswordContainer;
    private View mLoginContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginInput = (EditText) findViewById(R.id.login);

        mLoginContainer = findViewById(R.id.login_container);
        mPasswordContainer = findViewById(R.id.password_container);

        mPasswordInput = (EditText) findViewById(R.id.password);
        mPasswordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    doLogin();
                    return true;
                }
                return false;
            }
        });

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
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(@NonNull String email) {
        return email.length() > 5;
    }

    private boolean isPasswordValid(@NonNull String password) {
        return password.length() > 4;
    }

    private void showProgress(final boolean show) {
        mLoginContainer.setVisibility(show ? View.GONE : View.VISIBLE);
        animateAlphaForView(mLoginContainer, show);
        mPasswordContainer.setVisibility(show ? View.GONE : View.VISIBLE);
        animateAlphaForView(mPasswordContainer, show);

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        animateAlphaForView(mProgressView, !show);
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

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate some heavy work.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            return RANDOM_LOGIN[0].equals(mEmail) && RANDOM_LOGIN[1].equals(mPassword);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                shakeError();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        private void shakeError() {
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_anim);
            anim.reset();
            mLoginButton.clearAnimation();
            mLoginButton.startAnimation(anim);
        }
    }
}

