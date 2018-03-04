package com.possystem.waiterportal;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MenuActivity extends AppCompatActivity {

    private Spinner category;
    private ListView menu;
    private ArrayList<String> selecteditems = new ArrayList<String>();
    private FloatingActionButton cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selecteditems.add(getIntent().getExtras().getString("tableChoice"));
        this.setTitle(getIntent().getExtras().getString("tableChoice"));
        setContentView(R.layout.activity_menu);
        cart = (FloatingActionButton) findViewById(R.id.cart);
        cart.setEnabled(false);
        cart.setVisibility(View.INVISIBLE);
        BackgroundWorker bg = new BackgroundWorker(this);
        String type="category";
        try {
            ArrayList resultobtained = bg.execute(type).get();
            category = (Spinner) findViewById(R.id.category);
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, resultobtained);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            category.setAdapter(adapter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        category.setOnItemSelectedListener(itemOnClick);
        cart.setOnClickListener(categoryOnClick);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                selecteditems = data.getStringArrayListExtra("orderarray");
                if (selecteditems.size() == 1){
                    cart.setEnabled(false);
                    cart.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private View.OnClickListener categoryOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            Intent myIntent = new Intent(MenuActivity.this, CartActivity.class);
            myIntent.putStringArrayListExtra("selecteditems",selecteditems);
            startActivityForResult(myIntent, 1);
        }
    };

    private AdapterView.OnItemSelectedListener itemOnClick = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String type="menuitem";
            String chosencategory = parent.getItemAtPosition(position).toString();
            BackgroundWorker bg2 = new BackgroundWorker(MenuActivity.this);
            try {
                ArrayList resultobtained = bg2.execute(type,chosencategory).get();
                menu = (ListView) findViewById(R.id.menu);
                ArrayAdapter adapter = new ArrayAdapter(MenuActivity.this, android.R.layout.simple_list_item_1, resultobtained);
                menu.setAdapter(adapter);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            menu.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selecteditems.add(parent.getItemAtPosition(position).toString());
                    cart.setEnabled(true);
                    cart.show();
                    final Toast toast = Toast.makeText(getApplicationContext(),"Added", Toast.LENGTH_SHORT);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 500);
                }
            });
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
                //
        }
    };

}
