package com.example.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChooseStores extends AppCompatActivity {

    ListView listView;
    Button shopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_stores);

        listView = findViewById(R.id.listView);

        final String state = getIntent().getExtras().getString("state");

        //TODO get all stores name in state from backend
        final ArrayList<String> allStores = new ArrayList<>();
        allStores.add("Fox");
        allStores.add("H&M");
        allStores.add("H&O");
        allStores.add("Renuar");
        allStores.add("Castro");

        final ListViewAdapter arrayAdapter = new ListViewAdapter(this, allStores, false, 0);
        listView.setAdapter(arrayAdapter);
        listView.setItemsCanFocus(true);

        shopping = findViewById(R.id.shopping);
        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> positionCheckedBox = arrayAdapter.getCheckedBox();
                if (positionCheckedBox.size() == 0) {
                    Toast.makeText(getApplicationContext(), "please choose at least one store", Toast.LENGTH_LONG).show();
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString("state", state);

                int size = positionCheckedBox.size();
                for (int i = 0; i < size; i++)
                    bundle.putString("stores" + i, allStores.get(positionCheckedBox.get(i)));

                Intent intent = new Intent(ChooseStores.this, ResultActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
