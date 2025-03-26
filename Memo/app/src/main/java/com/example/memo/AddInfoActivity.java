package com.example.memo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.blankj.utilcode.util.UriUtils;
import com.bumptech.glide.Glide;
import com.example.memo.db.MyDbHelper;

import java.util.Calendar;

public class AddInfoActivity extends AppCompatActivity {
    private Button btn_choose, btn_save;
    private EditText edit_head, edit_text;
    private ImageView pit;
    private MyDbHelper db_helper;
    private SQLiteDatabase db;
    private String tmp_path, end_path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        init_view();
        choose_click();
        save_click();
    }
    private void init_view(){
        btn_choose = findViewById(R.id.choose_pit);
        btn_save = findViewById(R.id.save_btn);
        edit_head = findViewById(R.id.edit_title);
        edit_text = findViewById(R.id.edit_context);
        pit = findViewById(R.id.add_pit);

        db_helper = new MyDbHelper(AddInfoActivity.this);
        db = db_helper.getWritableDatabase();
    }

    private void choose_click(){
        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, 22);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 22:
                Uri imageuri = data.getData();
                if(imageuri == null)    return ;
                end_path = UriUtils.uri2File(imageuri).getPath();
                Glide.with(AddInfoActivity.this).load(end_path).into(pit);
                break;
            default:
                break;
        }
    }
    private void save_click(){
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                ContentValues contentValues = new ContentValues();
                contentValues.put("title", edit_head.getText().toString());
                contentValues.put("content", edit_text.getText().toString());
                contentValues.put("imgpath", end_path);
                contentValues.put("mtime", calendar.get(Calendar.YEAR) + "" + calendar.get(Calendar.MONTH) + "" + calendar.get(Calendar.DAY_OF_MONTH));
                db.insert("tb_memory", null, contentValues);
                Toast.makeText(AddInfoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddInfoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();;
            }
        });
    }
}