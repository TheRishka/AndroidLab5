package com.therishka.androidlab5;

import android.os.Bundle;
import android.support.annotation.StringRes;

public class ChangeUserDataActivity extends ToolbarActivity {

    public static final int RESULT_CANCEL = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_user_data);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCEL);
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    @StringRes
    public int getTitleTextResId() {
        return R.string.user_data_change_activity_title;
    }
}
