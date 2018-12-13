package com.example.user.lab11;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText book, price;
    private Button query, insert, update, delete;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();
    private SQLiteDatabase dbrw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        book = findViewById(R.id.book);
        price = findViewById(R.id.price);
        query = findViewById(R.id.query);
        insert = findViewById(R.id.insert);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        listView = findViewById(R.id.listview);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        dbrw = new MyDBHelper(this).getWritableDatabase();

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c;
                if (book.length() < 1)
                    c = dbrw.rawQuery("SELECT * FROM myTable", null);
                else
                    c = dbrw.rawQuery("SELECT*FROM myTable WHERE book LIKE '" + book.getText().toString() + "'", null);
                c.moveToFirst();
                items.clear();
                Toast.makeText(MainActivity.this, "共有" + c.getCount() + "筆資料", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < c.getCount(); i++) {
                    items.add("書名:" + c.getString(0) + "\t\t\t\t價格:" + c.getString(1));
                    c.moveToNext();
                }
                adapter.notifyDataSetChanged();
                c.close();
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (book.length() < 1 || price.length() < 1)
                    Toast.makeText(MainActivity.this, "欄位請勿留空", Toast.LENGTH_SHORT).show();
                else {
                    try {
                        dbrw.execSQL("INSERT INTO myTable(book,price)VALUES(?,?)", new Object[]{book.getText().toString(), price.getText().toString()});
                        Toast.makeText(MainActivity.this, "新增書名" + book.getText().toString() + "    價格" + price.getText().toString(), Toast.LENGTH_SHORT).show();
                        book.setText("");
                        price.setText("");
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "新增失敗:" + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (book.length() < 1 || price.length() < 1)
                    Toast.makeText(MainActivity.this, "欄位請勿留空", Toast.LENGTH_SHORT).show();
                else {
                    try {
                        dbrw.execSQL("UPDATE myTable SET price=" + price.getText().toString() + " WHERE book LIKE '" + book.getText().toString() + "'");
                        Toast.makeText(MainActivity.this, "更新書名" + book.getText().toString() + "    價格" + price.getText().toString(), Toast.LENGTH_SHORT).show();
                        book.setText("");
                        price.setText("");
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "更新失敗:" + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(book.length()<1)
                    Toast.makeText(MainActivity.this,"書名請勿留空",Toast.LENGTH_SHORT).show();
                else{
                    try{
                        dbrw.execSQL("DELETE FROM myTable WHERE book LIKE '"+book.getText().toString()+"'");
                        Toast.makeText(MainActivity.this,"刪除書名"+book.getText().toString(),Toast.LENGTH_SHORT).show();
                        book.setText("");
                        price.setText("");
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,"刪除失敗:"+e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    @Override
    public void onDestroy () {
        super.onDestroy();
        dbrw.close();
    }
}