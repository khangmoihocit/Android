package com.khangmoihocit.th_quanlybanhang;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText edtTitle, edtPrice, edtDescription;
    private Button btnAdd, btnUpdate;
    private SearchView searchView;
    private RecyclerView rcvProduct;
    private ProductAdapter productAdapter;
    private List<Product> listProduct;
    private ProductDAO productDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.search);
        edtTitle = findViewById(R.id.edt_title);
        edtPrice= findViewById(R.id.edt_price);
        edtDescription = findViewById(R.id.edt_description);
        btnAdd = findViewById(R.id.btn_add);
        btnUpdate = findViewById(R.id.btn_update);
        listProduct = new ArrayList<>();
        productDAO = AppDatabase.getInstance(MainActivity.this).productDAO();

        productAdapter = new ProductAdapter(new ProductAdapter.IClickItemProduct() {
            @Override
            public void delete(Product product) {
                clickDeleteProduct(product);
            }

            @Override
            public void clickItem(View view, int position) {
                clickItemProduct(view, position);
            }
        });
        rcvProduct = findViewById(R.id.rcv_product);
        rcvProduct.setLayoutManager(new LinearLayoutManager(this));
        rcvProduct.setAdapter(productAdapter);
        loadData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Product product = new Product();
                    product.setTitle(edtTitle.getText().toString());
                    product.setDescription(edtDescription.getText().toString());
                    product.setPrice(Double.parseDouble(edtPrice.getText().toString()));

                    productDAO.insert(product);
                    loadData();
                    clearInput();
                }catch (NumberFormatException ex){
                    Toast.makeText(MainActivity.this, "giá không đúng định dạng" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(currentProductId == -1){
                        Toast.makeText(MainActivity.this, "ban chua chon san pham de update", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Product product = productDAO.getById(currentProductId);
                    product.setTitle(edtTitle.getText().toString());
                    product.setDescription(edtDescription.getText().toString());
                    product.setPrice(Double.parseDouble(edtPrice.getText().toString()));

                    productDAO.update(product);
                    loadData();
                    btnAdd.setEnabled(true);
                    btnUpdate.setEnabled(false);
                    currentProductId = -1;
                    clearInput();
                }catch (NumberFormatException ex){
                    Toast.makeText(MainActivity.this, "giá không đúng định dạng" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                searchProduct(newText);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProduct(query);
                return false;
            }
        });

    }

    private void searchProduct(String keyword){
        List<Product> result = productDAO.findAll(keyword);
        productAdapter.setData(result);
    }

    private int currentProductId = -1;

    private void clickItemProduct(View view, int position) {
        btnAdd.setEnabled(false);
        btnUpdate.setEnabled(true);

        Product product = productAdapter.get(position);
        edtTitle.setText(product.getTitle());
        edtPrice.setText(product.getPrice().toString());
        edtDescription.setText(product.getDescription());
        currentProductId = product.getId();
    }

    private void clickDeleteProduct(Product product) {
        new AlertDialog.Builder(this)
                .setTitle("xoa san pham")
                .setMessage("ban chac chu")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        productDAO.delete(product);
                        loadData();
                        Toast.makeText(MainActivity.this, "xoa thanh cong", Toast.LENGTH_SHORT).show();
                        clearInput();
                    }
                })
                .setNegativeButton("no", null)
                .show();
    }

    private void loadData(){
        listProduct = productDAO.getAll();
        productAdapter.setData(listProduct);
    }
    
    private void clearInput(){
        edtTitle.setText("");
        edtPrice.setText("");
        edtDescription.setText("");
    }
}