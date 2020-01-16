package com.example.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ManagerActivity extends MainActivity {

    ImageButton user;

    TextView title;
    Button create;
    Button update;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        user = findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerActivity.this, UserInfo.class);
                intent.putExtras(new Bundle());
                startActivity(intent);
            }
        });

        title = findViewById(R.id.title);
        title.setText("Hello, " + loginUser.getUsername());

        create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCreating = true;
                intent = new Intent(ManagerActivity.this, CreateUpdateElement.class);
                intent.putExtras(new Bundle());
                startActivity(intent);
            }
        });

        update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCreating = false;
                intent = new Intent(ManagerActivity.this, ChooseUpdateElementBy.class);
                intent.putExtras(new Bundle());
                startActivity(intent);
            }
        });
    }
}
