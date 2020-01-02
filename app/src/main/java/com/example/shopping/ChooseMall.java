package com.example.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChooseMall extends AppCompatActivity {

    ListView listView;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mall);

        listView = findViewById(R.id.listView);
        title = findViewById(R.id.title);

        //TODO: get all malls from backend and put in allMalls
        final ArrayList<String> allMalls = new ArrayList<>();
        allMalls.add("Fox");
        allMalls.add("H&M");
        allMalls.add("H&O");
        allMalls.add("Renuar");
        allMalls.add("Castro");

        final int actionNumber = getIntent().getExtras().getInt("actionNumber");
        int multipleChoice = 1;
        if (actionNumber == 3) {
            multipleChoice = 2;
            title.setText("Please choose two malls you want to know the distance between");
        }

        final ListViewAdapter arrayAdapter = new ListViewAdapter(this, allMalls, true, multipleChoice);
        listView.setAdapter(arrayAdapter);
        listView.setItemsCanFocus(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                Bundle bundle = new Bundle();
                bundle.putString("state", getIntent().getExtras().getString("state"));

                switch (actionNumber) {
                    case 2:
//                        intent = new Intent(ChooseMall.this, ChooseCategory.class);
//                        bundle.putInt("actionNumber", from);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
                        break;
                    case 1:
                    case 4:
                        intent = new Intent(ChooseMall.this, ResultActivity.class);
                        bundle.putInt("actionNumber", actionNumber);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 3:
//                        intent = new Intent(ChooseMall.this, MallsDistance.class);
//                        bundle.putInt("actionNumber", actionNumber);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
                        break;
                }
            }
        });
    }
}
