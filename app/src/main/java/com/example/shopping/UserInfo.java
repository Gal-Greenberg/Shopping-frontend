package com.example.shopping;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.shopping.Tasks.UserTasks;
import com.example.shopping.User.UserBoundary;
import com.example.shopping.User.UserRole;

public class UserInfo extends MainActivity {

    EditText userName;
    EditText avatar;
    RadioGroup roles;

    RadioButton selectedRole;

    Button updateUser;
    Button signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        userName = findViewById(R.id.userName);
        avatar = findViewById(R.id.avatar);
        roles = findViewById(R.id.roles);

        insertInput();

        updateUser = findViewById(R.id.updateUser);
        updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInputValid()) {
                    return;
                }

                if (userName.getText().toString().matches(loginUser.getUsername()) &&
                        avatar.getText().toString().equals(loginUser.getAvatar()) &&
                        selectedRole.getText().toString().equals(loginUser.getRole().toString())) {
                    Toast.makeText(getApplicationContext(), "the user information didn't change", Toast.LENGTH_LONG).show();
                    return;
                }

                userTask = new UserTasks();
                String result = null;
                try {
                    result = (String) userTask.execute("update", "put", BASE_URL + "/users/{domain}/{userEmail}", DOMAIN, loginUser.getUserId().getEmail(),
                            selectedRole.getText().toString(), userName.getText().toString(), avatar.getText().toString()).get();
                    Log.d("restTemplate", loginUser.toString());
                } catch (Exception e) {
                    Log.e("ExceptionSignUp", e.getMessage());
                }

                if (result.matches("put result succeeded")) {
                    if (selectedRole.getText().toString().matches("PLAYER"))
                        loginUser = new UserBoundary(loginUser.getUserId(), UserRole.PLAYER, userName.getText().toString(),
                            avatar.getText().toString());
                    else
                        loginUser = new UserBoundary(loginUser.getUserId(), UserRole.MANAGER, userName.getText().toString(),
                                avatar.getText().toString());
                    actionSucceeded();
                }
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        });

        signOut = findViewById(R.id.signOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser = null;
                Intent intent = new Intent(UserInfo.this, MenuActivity.class);
                intent.putExtras(new Bundle());
                startActivity(intent);
            }
        });
    }

    public void actionSucceeded() {
        Toast.makeText(getApplicationContext(), "Successful user update", Toast.LENGTH_LONG).show();

        Intent intent;
        if (loginUser.getRole() == UserRole.PLAYER) {
            intent = new Intent(UserInfo.this, ChooseAction.class);
            intent.putExtras(new Bundle());
            startActivity(intent);
        } else { // MANAGER
            intent = new Intent(UserInfo.this, ManagerActivity.class);
            intent.putExtras(new Bundle());
            startActivity(intent);
        }
    }

    public void insertInput() {
        userName.setText(loginUser.getUsername());
        avatar.setText(loginUser.getAvatar());

        if (loginUser.getRole() == UserRole.PLAYER)
            ((RadioButton)roles.getChildAt(0)).setChecked(true);
        else
            ((RadioButton)roles.getChildAt(1)).setChecked(true);
    }

    public boolean isInputValid() {
        if (userName.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "please enter user name", Toast.LENGTH_LONG).show();
            return false;
        }
        if (avatar.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "please enter avatar", Toast.LENGTH_LONG).show();
            return false;
        }

        int selectedId = roles.getCheckedRadioButtonId();
        selectedRole = findViewById(selectedId);
        if (selectedRole == null) {
            Toast.makeText(getApplicationContext(), "please choose role", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
