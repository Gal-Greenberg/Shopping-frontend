package com.example.shopping;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopping.Tasks.ActionTasks;

import java.util.HashMap;
import java.util.Map;

public class MallsDistance extends MainActivity {

    ImageView car;
    TextView distance;
    Button main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_malls_distance);

        Map<String, Object> result;
        actionTasks = new ActionTasks();
        try {
            result = (HashMap) actionTasks.execute(BASE_URL + "/actions", DOMAIN, loginUser.getUserId().getEmail(),
                    selectedMalls.get(0).getElementId().getId(), "distanceBetweenMalls", "0", "10",
                    selectedMalls.get(1).getElementId().getId()).get();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        distance = findViewById(R.id.distance);
        distance.setText("The distance between " + selectedMalls.get(0).getName() + " to " + selectedMalls.get(1).getName()
                + " is " + String.format("%.2f", result.get("dis")) + " KM");

        car = findViewById(R.id.car);
        Animation animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.7f, TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f);
        animation.setDuration(2500);
        animation.setFillAfter(true);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setInterpolator(new LinearInterpolator());
        car.setAnimation(animation);

        main = findViewById(R.id.main);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(MallsDistance.this, ChooseAction.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
