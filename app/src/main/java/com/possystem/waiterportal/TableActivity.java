package com.possystem.waiterportal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class TableActivity extends AppCompatActivity {

    protected String choice;
    protected Button nextbtn;
    protected RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        this.setTitle("Select Table");
        nextbtn = (Button) findViewById(R.id.nextbtn);
        nextbtn.setOnClickListener(nextbtnListener);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(radioGroupListener);
    }

    private View.OnClickListener nextbtnListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(TableActivity.this, MenuActivity.class);
            myIntent.putExtra("tableChoice",choice);
            startActivity(myIntent);
        }
    };

    private RadioGroup.OnCheckedChangeListener radioGroupListener = new RadioGroup.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch(checkedId) {
                case R.id.radioButton1:
                    choice = "Table 1";
                    break;
                case R.id.radioButton2:
                    choice = "Table 2";
                    break;
                case R.id.radioButton3:
                    choice = "Table 3";
                    break;
                case R.id.radioButton4:
                    choice = "Table 4";
                    break;
                case R.id.radioButton5:
                    choice = "Table 5";
                    break;
            }
            nextbtn.setEnabled(true);
        }
    };

}
