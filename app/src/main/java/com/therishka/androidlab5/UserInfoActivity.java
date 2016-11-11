package com.therishka.androidlab5;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

/**
 * @author Rishad Mustafaev
 */

public class UserInfoActivity extends ToolbarActivity {

    public static final int CHANGE_USER_DATA_REQUEST_CODE = 123;

    TextView mUserName;
    TextView mUserSurname;
    TextView mUserAge;
    TextView mUserSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.user_home);
        super.onCreate(savedInstanceState);
        mUserName = (TextView) findViewById(R.id.user_data_name);
        mUserSurname = (TextView) findViewById(R.id.user_data_surname);
        mUserAge = (TextView) findViewById(R.id.user_data_age);
        mUserSex = (TextView) findViewById(R.id.user_data_sex);
    }

    public void changeUserDataBtnClick(View v) {
        startChangeUserDataActivity();
    }

    private void startChangeUserDataActivity() {
        Intent intent = new Intent(this, ChangeUserDataActivity.class);
        startActivityForResult(intent, CHANGE_USER_DATA_REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @StringRes
    @Override
    public int getTitleTextResId() {
        return R.string.user_activity_title;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
