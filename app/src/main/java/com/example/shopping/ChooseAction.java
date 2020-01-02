package com.example.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChooseAction extends AppCompatActivity {

    Spinner spinner;
    ListView listView;
    Button shopping;

    String spinnerSelected;
    int spinnerSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_action);

        listView = findViewById(R.id.listView);

        final ArrayList<String> allActions = new ArrayList<>();
        allActions.add("Finding all stores you want to go in the closest mall"); //0
        allActions.add("Finding all stores in specific mall"); //1
        allActions.add("Finding all stores in specific category in specific mall"); //2
        allActions.add("Finding distance between shopping malls"); //3
        allActions.add("Search the store with the most likes in specific mall"); //4

        final ListViewAdapter arrayAdapter = new ListViewAdapter(this, allActions, false, 1);
        listView.setAdapter(arrayAdapter);
        listView.setItemsCanFocus(true);

        spinner = findViewById(R.id.states);
        //TODO: get all stats from backend and put in spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Object item = adapterView.getItemAtPosition(position);
                spinnerSelected = item.toString();
                spinnerSelectedPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        shopping = findViewById(R.id.shopping);
        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> positionCheckedBox = arrayAdapter.getCheckedBox();
                if (positionCheckedBox.size() == 0) {
                    Toast.makeText(getApplicationContext(), "please choose action", Toast.LENGTH_LONG).show();
                    return;
                }
                if (spinnerSelectedPosition == 0) {
                    Toast.makeText(getApplicationContext(), "please choose state", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent;
                Bundle bundle = new Bundle();
                bundle.putString("state", spinnerSelected);
                bundle.putInt("actionNumber", positionCheckedBox.get(0));

                switch (positionCheckedBox.get(0)) {
                    case 0:
                        intent = new Intent(ChooseAction.this, ChooseStores.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        intent = new Intent(ChooseAction.this, ChooseMall.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}
