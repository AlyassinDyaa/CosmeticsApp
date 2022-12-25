package com.example.batyaa.tulan_cosmetics.ViewHolders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.batyaa.tulan_cosmetics.Classes.Product;
import com.example.batyaa.tulan_cosmetics.Classes.category;
import com.example.batyaa.tulan_cosmetics.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryViewHolder extends RecyclerView.ViewHolder
{
    View mView;
    Context mContext ;
    private List<category> mProducts;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;


        itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
        //item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });


    }

    //setting the details to the recycler row
    public void setDetails(Context ctx, String title, String image) {
        //Views
        TextView mTitleView = mView.findViewById(R.id.catTitle_id);
        ImageView mImageView = mView.findViewById(R.id.imageCategory);

        //Set data to views
        mTitleView.setText(title);
        Picasso.get().load(image).placeholder(R.drawable.load).fit().centerCrop().into(mImageView);
    }

    private ViewHolder.ClickListener mClickListener;

    //Interface to send callbacks
    public interface ClickListener{

        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }



}
