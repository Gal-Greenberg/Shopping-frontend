package com.example.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ManagerActivity extends MainActivity {

    TextView title;
    Button create;
    Button update;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        title = findViewById(R.id.title);
        title.setText("Hello, " + loginUser.getUsername());

        create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCreating = true;
                intent = new Intent(ManagerActivity.this, CreateElement.class);
                intent.putExtras(new Bundle());
                startActivity(intent);
            }
        });

        update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCreating = false;
                intent = new Intent(ManagerActivity.this, UpdateElement.class);
                intent.putExtras(new Bundle());
                startActivity(intent);
            }
        });
    }
}
