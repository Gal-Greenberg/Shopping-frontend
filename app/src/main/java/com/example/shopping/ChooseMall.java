package com.example.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
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

    int page;
    int sizeResult;
    ArrayList<ElementBoundary> allMalls;

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

        selectedMalls = new ArrayList<>();
        allMalls = new ArrayList<>();

        title = findViewById(R.id.title);
        int multipleChoice = 1;
        positionCheckedBoxEmptyErr = "please choose one mall";
        if (actionNumber == 3) {
            multipleChoice = 2;
            title.setText("Please choose two malls you want to know the distance between");
            positionCheckedBoxEmptyErr = "please choose two mall";
        }

        listView = findViewById(R.id.listView);
        isGetActionResults(true, true);
        final ListViewAdapter arrayAdapter = new ListViewAdapter(this, allMalls, true, "info", true, multipleChoice);
        listView.setAdapter(arrayAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstItem, int visibleItemCount, int totalItems) {
                boolean isScroll = false;
                boolean isPrev = false;
                if (firstItem == 0 && page > 0) {
                    page--;
                    saveSelectedMalls(arrayAdapter.getCheckedBox());
                    isScroll = isGetActionResults(false, false);
                    isPrev = true;
                } else if (sizeResult == 10 && (firstItem + visibleItemCount) == totalItems) {
                    page++;
                    saveSelectedMalls(arrayAdapter.getCheckedBox());
                    isScroll = isGetActionResults(true, false);
                }

                if (isScroll) {
                    listView.setAdapter(arrayAdapter);
                    listView.setSelection(totalItems);
                    arrayAdapter.notifyDataSetChanged();

                    if (isPrev) {
                        listView.setSelectionAfterHeaderView();

//                        for (int i = 0; i < selectedStores.size(); i++) {
//                            int indexInListView = allStores.indexOf(selectedStores.get(i));
//                            listView.setSelection(indexInListView);
//                        }
                    }
                }
            }
        });

        shopping = findViewById(R.id.shopping);
        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> positionCheckedBox = arrayAdapter.getCheckedBox();
                if (positionCheckedBox.size() == 0) {
                    Toast.makeText(getApplicationContext(), positionCheckedBoxEmptyErr, Toast.LENGTH_LONG).show();
                    return;
                }

                saveSelectedMalls(arrayAdapter.getCheckedBox());

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
                        if (selectedMalls.size() < 2) {
                            Toast.makeText(getApplicationContext(), positionCheckedBoxEmptyErr, Toast.LENGTH_LONG).show();
                            return;
                        }
                        intent = new Intent(ChooseMall.this, MallsDistance.class);
                        intent.putExtras(new Bundle());
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    public boolean isGetActionResults(boolean isNextPage, boolean isFirstTime) {
        elementTasks = new ElementTasks();
        Object result;
        try {
            result = elementTasks.execute("malls", "get", BASE_URL + "/elements/{userDomain}/{userEmail}/byType/{type}?size={size}&page={page}",
                    DOMAIN, loginUser.getUserId().getEmail(), "mall", PAGE_SIZE, "" + page).get();
            if (result.getClass() == String.class) {
                Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
                return false;
            }

            ElementBoundary[] resultArr = (ElementBoundary[]) result;
            sizeResult = resultArr.length;

            if (isNextPage) {
                for (int i = 0; i < sizeResult; i++) {
                    if (((String) resultArr[i].getElementAttributes().get("state")).matches(selectedState.getName())) {
                        if (!isFirstTime)
                            allMalls.remove(i);
                        allMalls.add(resultArr[i]);
                    }
                }
            } else {
                for (int i = sizeResult - 1; i > -1; i--) {
                    if (((String) resultArr[i].getElementAttributes().get("state")).matches(selectedState.getName())) {
                        allMalls.remove(i);
                        allMalls.add(resultArr[i]);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            Log.e("ExceptionSearchUpdate", e.getMessage());
            return false;
        }
    }

    public void saveSelectedMalls(List<Integer> positionCheckedBox) {
        int size = positionCheckedBox.size();
        for (int i = 0; i < size; i++)
            selectedMalls.add(allMalls.get(positionCheckedBox.get(i)));
    }
}
