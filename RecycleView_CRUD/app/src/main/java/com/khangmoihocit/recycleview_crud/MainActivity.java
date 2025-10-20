package com.khangmoihocit.recycleview_crud;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khangmoihocit.recycleview_crud.model.Cat;
import com.khangmoihocit.recycleview_crud.model.CatAdapter;
import com.khangmoihocit.recycleview_crud.model.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements CatAdapter.CatItemListener,
        SearchView.OnQueryTextListener {
    private Spinner spinner;
    private RecyclerView recyclerView;
    private CatAdapter catAdapter;
    private EditText eName, eDescribe, ePrice;
    private Button btnAdd, btnUpdate;
    private SearchView searchView;
    private final int []imgs = {R.drawable.img, R.drawable.img_1, R.drawable.img_2, R.drawable.img_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        catAdapter = new CatAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(catAdapter);

        catAdapter.setCatItemListener(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cat cat = new Cat();

                int img = R.drawable.img_1;
                double price = 0;
                try{
                    //lấy ảnh theo vị trí
                    int i = spinner.getSelectedItemPosition();
                    if(i<0 || i >= imgs.length) {
                        Toast.makeText(MainActivity.this, "Vui lòng chọn ảnh hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    img = imgs[i];

                    price = Double.parseDouble(ePrice.getText().toString());
                    cat.setName(eName.getText().toString());
                    cat.setDescribe(eDescribe.getText().toString());
                    cat.setPrice(price);
                    cat.setImg(img);
                    catAdapter.add(cat);
                }
                catch(NumberFormatException ex){
                    Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
                catch (Exception ex){
                    Toast.makeText(MainActivity.this, "class: " + ex.getClass()
                            +" meg: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cat cat = new Cat();

                int img = R.drawable.img_1;
                double price = 0;
                try{
                    //lấy ảnh theo vị trí
                    int i = spinner.getSelectedItemPosition();
                    if(i<0 || i >= imgs.length) {
                        Toast.makeText(MainActivity.this, "Vui lòng chọn ảnh hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    img = imgs[i];

                    price = Double.parseDouble(ePrice.getText().toString());
                    cat.setName(eName.getText().toString());
                    cat.setDescribe(eDescribe.getText().toString());
                    cat.setPrice(price);
                    cat.setImg(img);
                    catAdapter.update(positionCurrent, cat);
                    btnAdd.setEnabled(true);
                    btnUpdate
                            .setEnabled(false);
                }
                catch(NumberFormatException ex){
                    Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
                catch (Exception ex){
                    Toast.makeText(MainActivity.this, "class: " + ex.getClass()
                            +" meg: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void init(){
        spinner = findViewById(R.id.img);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this);
        spinner.setAdapter(spinnerAdapter);

        recyclerView = findViewById(R.id.recycleView);
        eName = findViewById(R.id.edName);
        eDescribe = findViewById(R.id.edDescribe);
        ePrice = findViewById(R.id.edPrice);
        searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setEnabled(false);
    }


    private int positionCurrent;
    @Override
    public void onItemClick(View view, int position) {
        btnAdd.setEnabled(false);
        btnUpdate.setEnabled(true);

        positionCurrent = position;
        Cat cat = catAdapter.get(positionCurrent);

        eName.setText(cat.getName());
        eDescribe.setText(cat.getDescribe());
        ePrice.setText(cat.getPrice()+"");
        for(int i=0; i<imgs.length; ++i){
            if(cat.getImg() == imgs[i]){
                spinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filter(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public void filter(String keyword){
        List<Cat> cats = catAdapter.getListBackup().stream()
                .filter(cat -> cat.getName() != null &&
                        cat.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        if(cats.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show();
        }else{
            catAdapter.filter(cats);
        }
    }
}