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
import android.widget.Toast;

import com.example.shopping.Tasks.UserTasks;
import com.example.shopping.User.UserBoundary;
import com.example.shopping.User.UserRole;

public class SignIn extends MainActivity {

    EditText email;
    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.email);

        signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "please enter email", Toast.LENGTH_LONG).show();
                    return;
                }

                userTask = new UserTasks();
                Object result = null;
                try {
                    result = userTask.execute("login", "get", BASE_URL + "/users/login/{domain}/{email}", DOMAIN,
                            email.getText().toString()).get();
                    Log.d("restTemplate", loginUser.toString());
                } catch (Exception e) {
                    Log.e("ExceptionSignIn", e.getMessage());
                }

                if (result.getClass() == String.class) {
                    Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
                    return;
                }
                loginUser = (UserBoundary) result;

                Intent intent;
                if (loginUser.getRole() == UserRole.PLAYER) { // PLAYER
                    intent = new Intent(SignIn.this, ChooseAction.class);
                    intent.putExtras(new Bundle());
                    startActivity(intent);
                } else { // MANAGER
                    intent = new Intent(SignIn.this, ManagerActivity.class);
                    intent.putExtras(new Bundle());
                    startActivity(intent);
                }
            }
        });
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
