package com.example.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shopping.Element.ElementBoundary;
import com.example.shopping.Tasks.ElementTasks;

import java.util.ArrayList;
import java.util.List;

public class ChooseAction extends MainActivity {

    ImageButton user;

    Spinner spinner;
    ListView listView;
    Button shopping;

    int spinnerSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_action);

        user = findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseAction.this, UserInfo.class);
                intent.putExtras(new Bundle());
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.listView);

        final ArrayList<String> allActions = new ArrayList<>();
        allActions.add("Finding all stores you want to go in the closest mall"); //0
        allActions.add("Finding all stores in specific mall"); //1
        allActions.add("Finding all stores in specific category in specific mall"); //2
        allActions.add("Finding distance between shopping malls"); //3
        allActions.add("Search the store with the most likes in specific mall"); //4

        final ListViewAdapter listViewAdapter = new ListViewAdapter(this, allActions, false, "", true, 1);
        listView.setAdapter(listViewAdapter);

        Object result = null;
        elementTasks = new ElementTasks();
        try {
            result = elementTasks.execute("states", "get", BASE_URL + "/elements/{userDomain}/{userEmail}/byType/{type}",
                    DOMAIN, loginUser.getUserId().getEmail(), "state").get();
        } catch (Exception e) {
            Log.e("ExceptionChooseAction", e.getMessage());
        }

        if (result.getClass() == String.class) {
            Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        final ElementBoundary[] resultElementBoundary = (ElementBoundary[]) result;

        String[] arraySpinner = new String[resultElementBoundary.length + 1];
        arraySpinner[0] = "Choose a state";
        for (int i = 0; i < resultElementBoundary.length; i++) {
            arraySpinner[i + 1] = resultElementBoundary[i].getName();
        }

        spinner = findViewById(R.id.states);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                arraySpinner);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stringArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                spinnerSelectedPosition = position;
                if (position == 0)
                    selectedState = null;
                else
                    selectedState = resultElementBoundary[position - 1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        shopping = findViewById(R.id.shopping);
        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> positionCheckedBox = listViewAdapter.getCheckedBox();
                if (positionCheckedBox.size() == 0) {
                    Toast.makeText(getApplicationContext(), "please choose action", Toast.LENGTH_LONG).show();
                    return;
                }
                if (spinnerSelectedPosition == 0) {
                    Toast.makeText(getApplicationContext(), "please choose state", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent;
                actionNumber = positionCheckedBox.get(0);
                switch (actionNumber) {
                    case 0:
                        intent = new Intent(ChooseAction.this, ChooseStores.class);
                        intent.putExtras(new Bundle());
                        startActivity(intent);
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        intent = new Intent(ChooseAction.this, ChooseMall.class);
                        intent.putExtras(new Bundle());
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}
