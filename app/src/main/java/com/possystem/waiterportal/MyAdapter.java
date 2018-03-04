package com.possystem.waiterportal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<String> mData;
    private ArrayList<String> mDataSet;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        protected TextView mTextView;
        protected EditText mQuantitytView;
        protected Button mDeleteBtn;
        protected Button mInccBtn;
        protected Button mDeccBtn;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.list_item_string);
            mQuantitytView = (EditText) v.findViewById(R.id.qtyeditText);
            mDeleteBtn = (Button) v.findViewById(R.id.delete_btn);
            mInccBtn = (Button) v.findViewById(R.id.incc);
            mDeccBtn = (Button) v.findViewById(R.id.decc);
        }
    }

    public MyAdapter(ArrayList<String> myDataset, Context context) {
        mData = myDataset;
        this.context = context;
        mDataSet = new ArrayList<String>(new LinkedHashSet<String>(mData));
    }


    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_cardview, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {
        holder.mTextView.setText(mDataSet.get(position));
        holder.mQuantitytView.setText(Integer.toString(Collections.frequency(mData,mDataSet.get(position))));
        holder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (context instanceof CartActivity){
                    for(int i = ((CartActivity)context).orderarray.size() - 1 ;i > 0;i-- ){
                        if(((CartActivity)context).orderarray.get(i).equals(mDataSet.get(holder.getAdapterPosition()))){
                            ((CartActivity)context).orderarray.remove(i);
                        }
                    }
                }
                mDataSet.remove(holder.getAdapterPosition());
                if (getItemCount() == 0){
                    ((CartActivity)context).order.setEnabled(false);
                    ((CartActivity)context).order.hide();
                }
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
        holder.mInccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CartActivity)context).orderarray.add(mDataSet.get(holder.getAdapterPosition()));
                holder.mQuantitytView.setText(Integer.toString(Integer.parseInt(String.valueOf(holder.mQuantitytView.getText()))+1));
            }
        });
        holder.mDeccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( Integer.parseInt(String.valueOf(holder.mQuantitytView.getText())) != 1 ){
                    ((CartActivity)context).orderarray.remove(mDataSet.get(holder.getAdapterPosition()));
                    holder.mQuantitytView.setText(Integer.toString(Integer.parseInt(String.valueOf(holder.mQuantitytView.getText()))-1));
                }
                else {
                    holder.mQuantitytView.setText(Integer.toString(1));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
