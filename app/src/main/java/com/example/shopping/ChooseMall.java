package com.example.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopping.Element.ElementBoundary;
import com.example.shopping.Tasks.ElementTasks;

import java.util.ArrayList;
import java.util.List;

public class ChooseMall extends MainActivity {

    ImageButton user;

    ListView listView;
    TextView title;
    Button shopping;

    String positionCheckedBoxEmptyErr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mall);

        user = findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseMall.this, UserInfo.class);
                intent.putExtras(new Bundle());
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.listView);
        title = findViewById(R.id.title);

        Object result = null;
        elementTasks = new ElementTasks();
        try {
            result = elementTasks.execute("malls", "get", BASE_URL + "/elements/{userDomain}/{userEmail}/byType/{type}",
                    DOMAIN, loginUser.getUserId().getEmail(), "mall").get();
        } catch (Exception e) {
            Log.e("ExceptionChooseMall", e.getMessage());
        }

        if (result.getClass() == String.class) {
            Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        ElementBoundary[] resultElementBoundary = (ElementBoundary[]) result;

        final ArrayList<ElementBoundary> allMalls = new ArrayList<>();
        for (ElementBoundary mall: resultElementBoundary) {
            if (mall.getElementAttributes().get("state").equals(selectedState)){
                allMalls.add(mall);
            }
        }

        int multipleChoice = 1;
        positionCheckedBoxEmptyErr = "please choose one mall";
        if (actionNumber == 3) {
            multipleChoice = 2;
            title.setText("Please choose two malls you want to know the distance between");
            positionCheckedBoxEmptyErr = "please choose two mall";
        }

        final ListViewAdapter arrayAdapter = new ListViewAdapter(this, allMalls, true, "info", true, multipleChoice);
        listView.setAdapter(arrayAdapter);

        shopping = findViewById(R.id.shopping);
        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> positionCheckedBox = arrayAdapter.getCheckedBox();
                if (positionCheckedBox.size() == 0) {
                    Toast.makeText(getApplicationContext(), positionCheckedBoxEmptyErr, Toast.LENGTH_LONG).show();
                    return;
                }

                int size = positionCheckedBox.size();
                selectedMalls = new ElementBoundary[size];
                for (int i = 0; i < size; i++)
                    selectedMalls[i] = allMalls.get(positionCheckedBox.get(i));

                Intent intent;
                switch (actionNumber) {
                    case 2:
                        intent = new Intent(ChooseMall.this, ChooseCategory.class);
                        intent.putExtras(new Bundle());
                        startActivity(intent);
                        break;
                    case 1:
                    case 4:
                        intent = new Intent(ChooseMall.this, ResultActivity.class);
                        intent.putExtras(new Bundle());
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(ChooseMall.this, MallsDistance.class);
                        intent.putExtras(new Bundle());
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}
