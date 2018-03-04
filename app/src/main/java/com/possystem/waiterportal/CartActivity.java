package com.possystem.waiterportal;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CartActivity extends AppCompatActivity{

    protected ArrayList<String> orderarray;
    protected ArrayList<String> list;
    protected FloatingActionButton order;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        this.setTitle("Order Summary");
        orderarray = getIntent().getStringArrayListExtra("selecteditems");
        order = (FloatingActionButton) findViewById(R.id.order);
        order.setOnClickListener(orderClick);
        list = new ArrayList(orderarray.subList(1, orderarray.size()));
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(list,this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putStringArrayListExtra("orderarray", orderarray);
        setResult(RESULT_OK, intent);
        finish();
    }

    protected View.OnClickListener orderClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            BackgroundWorker bg = new BackgroundWorker(CartActivity.this);
            try {
                String[] data = new String[orderarray.size()];
                data = orderarray.toArray(data);
                ArrayList<String> result = bg.execute(data).get();
                if(result.get(0).equals("success")){
                    Intent myIntent = new Intent(CartActivity.this, TableActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(myIntent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Unable to connect to server", Toast.LENGTH_LONG).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    };

}
