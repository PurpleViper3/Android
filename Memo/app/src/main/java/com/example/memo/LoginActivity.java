package com.example.memo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {
    private EditText input_username, input_password;
    private CheckBox check_rem;
    private Button cancel, login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        input_username = findViewById(R.id.input_username);
        input_password = findViewById(R.id.input_password);
        check_rem = findViewById(R.id.check_rem);
        cancel = findViewById(R.id.cancel);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("user_info", 0).edit();
                editor.putString("username", input_username.getText().toString());
                editor.putString("password", input_password.getText().toString());
                editor.putBoolean("rem", check_rem.isChecked());
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        displayInfo();
    }

    private void displayInfo(){
        boolean store = getSharedPreferences("user_info", 0).getBoolean("rem", false);
        if(store){
            String strname = getSharedPreferences("user_info", 0).getString("username", "");
            String strpass = getSharedPreferences("user_info", 0).getString("password", "");
            input_username.setText(strname);
            input_password.setText(strpass);
            check_rem.setChecked(true);
        }
    }
}