package com.example.smarthomeku;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class RegisterActivity extends AppCompatActivity {
    EditText TxUsername, TxPassword, TxConPassword;
    Button BtnRegister;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbHelper = new DBHelper(this);

        TxUsername = (EditText)findViewById(R.id.txUsernameReg);
        TxPassword = (EditText)findViewById(R.id.txPasswordReg);
        TxConPassword = (EditText)findViewById(R.id.txConPassword);
        BtnRegister = (Button)findViewById(R.id.btnRegister);

        TextView tvRegister = (TextView)findViewById(R.id.tvRegister);

        tvRegister.setText(fromHtml("<font color='#62A4F1'>Kembali untuk masuk</font>"));

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = TxUsername.getText().toString().trim();
                String password = TxPassword.getText().toString().trim();
                String conPassword = TxConPassword.getText().toString().trim();

                ContentValues values = new ContentValues();


                if (!password.equals(conPassword)){
                    Toast.makeText(RegisterActivity.this, "Kata sandi tidak cocok!", Toast.LENGTH_SHORT).show();
                }else if (password.equals("") || username.equals("")){
                    Toast.makeText(RegisterActivity.this, "Nama pengguna atau kata sandi harus diisi!", Toast.LENGTH_SHORT).show();
                }else {
                    values.put(DBHelper.row_username, username);
                    values.put(DBHelper.row_password, password);
                    dbHelper.insertData(values);

                    Toast.makeText(RegisterActivity.this, "Pendaftaran Berhasil", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


    }

    public static Spanned fromHtml(String html){
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        }else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}