package com.example.memo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.memo.bean.MemoAdapter;
import com.example.memo.bean.MemoBean;
import com.example.memo.db.MyDbHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btn_add;
    private RecyclerView rec_view;
    private MyDbHelper helper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_add = findViewById(R.id.btn_add);
        rec_view = findViewById(R.id.recycle_view);
        helper = new MyDbHelper(MainActivity.this);
        db = helper.getWritableDatabase();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        item_display();
    }

    private void item_display(){
        List<MemoBean> arr = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from tb_memory", null);
        while(cursor.moveToNext()){
            int titleIndex = cursor.getColumnIndex("title");
            int contentIndex = cursor.getColumnIndex("content");
            int imgIndex = cursor.getColumnIndex("imgpath");
            int timeIndex = cursor.getColumnIndex("time");

            String item_title = titleIndex != -1 ? cursor.getString(titleIndex) : "";
            String item_content = contentIndex != -1 ? cursor.getString(contentIndex) : "";
            String item_img = imgIndex != -1 ? cursor.getString(imgIndex) : "";
            String item_time = timeIndex != -1 ? cursor.getString(timeIndex) : "";
            MemoBean memoBean = new MemoBean(item_title, item_content, item_img, item_time);
            arr.add(memoBean);
        }

        cursor.close();

        MemoAdapter adapter = new MemoAdapter(MainActivity.this, arr);
        StaggeredGridLayoutManager st = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rec_view.setLayoutManager(st);
        rec_view.setAdapter(adapter);
    }
}