package com.example.shopping;

import androidx.appcompat.app.AppCompatActivity;

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

import com.example.shopping.User.UserBoundary;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

public class SignIn extends AppCompatActivity {

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

                Task login = new Task();
                try {
                    UserBoundary result = login.execute().get();
                    Log.d("restTemplate", result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int role = 0; // 0 - PLAYER, 1 - MANAGER

                Bundle bundle = new Bundle();
                Intent intent;

                bundle.putString("email", email.toString());

                if (role == 0) { // PLAYER
                    intent = new Intent(SignIn.this, ChooseAction.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else { // MANAGER
//                    intent = new Intent(SignIn.this, ManagerActivity.class);
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
}
