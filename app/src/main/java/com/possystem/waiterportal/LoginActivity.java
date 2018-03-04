package com.possystem.waiterportal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    protected EditText EmployeeIdEt, PasswordEt;
    protected Button btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Sign In");
        EmployeeIdEt = (EditText)findViewById(R.id.etempid);
        PasswordEt = (EditText)findViewById(R.id.etpassword);
        btnlogin = (Button)findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(loginClick);
    }

    protected View.OnClickListener loginClick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            String employeeid= EmployeeIdEt.getText().toString();
            String password= PasswordEt.getText().toString();
            String type="login";
            BackgroundWorker backgroundworker = new BackgroundWorker(LoginActivity.this);
            try {
                ArrayList<String> result = backgroundworker.execute(type,employeeid,password).get();
                if (result.get(0).equals("login success")){
                    Intent intent = new Intent(LoginActivity.this, TableActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Invalid Employee ID or Password", Toast.LENGTH_LONG).show();
                }
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "Server Unreachable", Toast.LENGTH_LONG).show();
            }  catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    };

}
