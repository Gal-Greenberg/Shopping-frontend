package com.example.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shopping.Element.ElementBoundary;
import com.example.shopping.Tasks.ActionTasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultActivity extends MainActivity {

    ListView listView;
    Button main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        listView = findViewById(R.id.listView);

        Object result = null;
        actionTasks = new ActionTasks();
        try {
            switch (actionNumber) {
                case 0:
                    result = elementTasks.execute(BASE_URL + "/actions", DOMAIN, loginUser.getUserId().getEmail(),
                            selectedState.getElementId().getId(), "findStoresInState", "0", "10", selectedStores.toString()).get();
                    break;
                case 1:
                    result = elementTasks.execute(BASE_URL + "/actions", DOMAIN, loginUser.getUserId().getEmail(),
                            selectedMalls[0].getElementId().getId(), "findAllStoresInMall", "0", "10").get();
                    break;
                case 2:
                    result = elementTasks.execute(BASE_URL + "/actions", DOMAIN, loginUser.getUserId().getEmail(),
                            selectedMalls[0].getElementId().getId(), "findAllStoresInCategoryInMall", "0", "10",
                            selectedCategory.toString()).get();
                    break;
                case 4:
                    result = elementTasks.execute(BASE_URL + "/actions", DOMAIN, loginUser.getUserId().getEmail(),
                            selectedMalls[0].getElementId().getId(), "findAllStoresByLikes", "0", "10").get();
                    break;
            }

        } catch (Exception e) {
            Log.e("ExceptionChooseAction", e.getMessage());
        }
        if (result.getClass() == String.class) {
            Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        List<ElementBoundary> allResults = Arrays.asList((ElementBoundary[]) result);

        final ListViewAdapter arrayAdapter = new ListViewAdapter(this, allResults, true, "info",
                false, 0);
        listView.setAdapter(arrayAdapter);

        main = findViewById(R.id.main);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(ResultActivity.this, ChooseAction.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
