package com.khangmoihocit.recycleview;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Grid;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khangmoihocit.recycleview.model.Cat;
import com.khangmoihocit.recycleview.model.CatAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CatAdapter catAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rview);
        catAdapter = new CatAdapter(this, getList());
        GridLayoutManager manager = new GridLayoutManager(this, 3); //spanCount - số cột
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(catAdapter);

    }

    private List<Cat> getList(){
        List<Cat> list = new ArrayList<>();
        list.add(new Cat(R.drawable.img, "meo 1"));
        list.add(new Cat(R.drawable.img_1, "meo 2"));
        list.add(new Cat(R.drawable.img_2, "meo 3"));
        list.add(new Cat(R.drawable.img_3, "meo 4"));
        return list;
    }
}