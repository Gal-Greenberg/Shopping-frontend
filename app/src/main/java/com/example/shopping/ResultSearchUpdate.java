package com.example.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ResultSearchUpdate extends MainActivity {

    ImageButton user;

    TextView title;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search_update);

        user = findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultSearchUpdate.this, UserInfo.class);
                intent.putExtras(new Bundle());
                startActivity(intent);
            }
        });

        title = findViewById(R.id.title);
        title.setText("Hello, " + loginUser.getUsername());

        listView = findViewById(R.id.listView);
        final ListViewAdapter arrayAdapter = new ListViewAdapter(this, resultSearchUpdate, true, "update", false, 0);
        listView.setAdapter(arrayAdapter);
    }
}
