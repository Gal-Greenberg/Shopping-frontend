package com.example.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    EditText email;
    EditText userName;
    EditText avatar;
    RadioGroup roles;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.email);
        userName = findViewById(R.id.userName);
        avatar = findViewById(R.id.avatar);

        roles = findViewById(R.id.roles);

        signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInputValid()) {
                    return;
                }

                //TODO sign up the user with backend
                int role = 0; // 0 - PLAYER, 1 - MANAGER

                Bundle bundle = new Bundle();
                Intent intent;

                bundle.putString("email", email.toString());

                if (role == 0) { // PLAYER
                    intent = new Intent(SignUp.this, ChooseAction.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else { // MANAGER
//                    intent = new Intent(SignUp.this, ManagerActivity.class);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
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

    public boolean isInputValid() {
        if (email.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "please enter email", Toast.LENGTH_LONG).show();
            return false;
        }
        if (userName.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "please enter user name", Toast.LENGTH_LONG).show();
            return false;
        }
        if (avatar.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "please enter avatar", Toast.LENGTH_LONG).show();
            return false;
        }

        int selectedId = roles.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        if (radioButton == null) {
            Toast.makeText(getApplicationContext(), "please choose role", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
