package com.example.batyaa.tulan_cosmetics.ViewHolders;



import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.batyaa.tulan_cosmetics.FIREBASE_CATEGORY.ItemsActivity;
import com.example.batyaa.tulan_cosmetics.Classes.Product;
import com.example.batyaa.tulan_cosmetics.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewHolder extends RecyclerView.ViewHolder
{
    ItemsActivity itemsOnClick;
    View mView;
    Context mContext ;
    private List<Product> mProducts;

    public ViewHolder(@NonNull View itemView)
    {
        super(itemView);
        mView = itemView;
        //item click

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
    public void setDetails(Context ctx, String title, String description, String image, String price, String Category,String size , String stock) {
        //Views
        TextView mTitleView = mView.findViewById(R.id.nameTextView);
        TextView mDetailsView = mView.findViewById(R.id.descriptionTextView);
        TextView mPriceVIew = mView.findViewById(R.id.priceRTextView);
        TextView mCat = mView.findViewById(R.id.categoryTextView);
        ImageView mImageView = mView.findViewById(R.id.cardImageView);
        TextView mSize = mView.findViewById(R.id.sizeTextView);
        TextView mStock = mView.findViewById(R.id.stockTextViewCardView);

        //Set data to views
        mTitleView.setText(title);
        mDetailsView.setText(description);
        Picasso.get().load(image).placeholder(R.drawable.load).fit().centerCrop().into(mImageView);
        mPriceVIew.setText(price);
        mCat.setText(Category);
        mSize.setText(size);
        mStock.setText(stock);
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
