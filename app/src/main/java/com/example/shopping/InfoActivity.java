package com.example.shopping;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class InfoActivity extends MainActivity {

    TextView title;
    TextView name;
    TextView state;

    TextView location;

    TextView mallAndFloor;
    TextView category;
    TextView numLikes;
    ImageButton like;

    int numberLikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        title = findViewById(R.id.title);
        name = findViewById(R.id.name);
        state = findViewById(R.id.state);
        location = findViewById(R.id.location);
        mallAndFloor = findViewById(R.id.mallAndFloor);
        category = findViewById(R.id.category);
        numLikes = findViewById(R.id.numLikes);
        like = findViewById(R.id.like);

        if (selectedInfo.getType().matches("mall"))
            infoAboutMall();
        else
            infoAboutStore();
        name.setText(selectedInfo.getName());
        state.setText("State: " + selectedInfo.getElementAttributes().get("state"));

    }

    public void infoAboutMall() {
        title.setText("Information about mall" );
        location.setText("Location: " + selectedInfo.getElementAttributes().get("location"));

        mallAndFloor.setVisibility(View.GONE);
        category.setVisibility(View.GONE);
        numLikes.setVisibility(View.GONE);
        like.setImageResource(R.drawable.clipart);
    }

    public void infoAboutStore() {
        title.setText("Information about store" );
        mallAndFloor.setText("Mall: " + selectedInfo.getElementAttributes().get("mall")
                + "Floor: " + selectedInfo.getElementAttributes().get("floor"));
        category.setText("Category: " + selectedInfo.getElementAttributes().get("category"));

        numberLikes = (int) selectedInfo.getElementAttributes().get("likes");
        numLikes.setText(numberLikes + "Likes");

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberLikes++;
                //TODO actione like to store
            }
        });

        location.setVisibility(View.GONE);
    }
}
