package com.example.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shopping.Element.ElementBoundary;
import com.example.shopping.Tasks.ElementTasks;

import java.util.ArrayList;
import java.util.List;

public class ChooseCategory extends MainActivity {

    ImageButton user;

    ListView listView;
    Button shopping;

    int page;
    int sizeResult;
    ArrayList<String> allCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);

        user = findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseCategory.this, UserInfo.class);
                intent.putExtras(new Bundle());
                startActivity(intent);
            }
        });

        selectedCategory = new ArrayList<>();
        allCategory = new ArrayList<>();

        listView = findViewById(R.id.listView);
        isGetActionResults(true, true);
        final ListViewAdapter arrayAdapter = new ListViewAdapter(this, allCategory, false, "", true, 0);
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
                    saveSelectedCategory(arrayAdapter.getCheckedBox());
                    isScroll = isGetActionResults(false, false);
                    isPrev = true;
                } else if (sizeResult == 10 && (firstItem + visibleItemCount) == totalItems) {
                    page++;
                    saveSelectedCategory(arrayAdapter.getCheckedBox());
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
                    Toast.makeText(getApplicationContext(), "please choose at least one category", Toast.LENGTH_LONG).show();
                    return;
                }

                saveSelectedCategory(arrayAdapter.getCheckedBox());

                Intent intent = new Intent(ChooseCategory.this, ResultActivity.class);
                intent.putExtras( new Bundle());
                startActivity(intent);
            }
        });
    }

    public boolean isGetActionResults(boolean isNextPage, boolean isFirstTime) {
        elementTasks = new ElementTasks();
        Object result;
        try {
            result = elementTasks.execute("category", "get",
                    BASE_URL + "/elements/{userDomain}/{userEmail}/byParent/{parentDomain}/{parentId}", DOMAIN,
                    loginUser.getUserId().getEmail(), DOMAIN, selectedMalls.get(0).getElementId().getId(), PAGE_SIZE, "" + page).get();
            if (result.getClass() == String.class) {
                Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
                return false;
            }

            ElementBoundary[] resultArr = (ElementBoundary[]) result;
            sizeResult = resultArr.length;

            if (isNextPage) {
                for (int i = 0; i < sizeResult; i++) {
                    String resultCategory = (String) resultArr[i].getElementAttributes().get("category");
                    if (!allCategory.contains(resultCategory)) {
                        if (!isFirstTime)
                            allCategory.remove(i);
                        allCategory.add(resultCategory);
                    }
                }
            } else {
                for (int i = sizeResult - 1; i > -1; i--) {
                    String resultCategory = (String) resultArr[i].getElementAttributes().get("category");
                    if (!allCategory.contains(resultCategory)) {
                        allCategory.remove(i);
                        allCategory.add(resultCategory);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            Log.e("ExceptionSearchUpdate", e.getMessage());
            return false;
        }
    }

    public void saveSelectedCategory(List<Integer> positionCheckedBox) {
        int size = positionCheckedBox.size();
        for (int i = 0; i < size; i++)
            selectedCategory.add(allCategory.get(positionCheckedBox.get(i)));
    }
}
