package com.example.testing.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testing.R;
import com.example.testing.interfaces.ItemClickListner;

public class ViewProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView addProductName,addProductPrice;
    private ItemClickListner itemClickListner;
    public ImageView addProductImg;

    public ViewProductHolder(@NonNull View itemView) {
        super(itemView);

        addProductName=itemView.findViewById(R.id.product_name);
        addProductPrice=itemView.findViewById(R.id.product_price);
        addProductImg=itemView.findViewById(R.id.product_image);
    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view,getAdapterPosition(), false);

    }

    public void setItemClickListner(ItemClickListner itemClickListner){
        this.itemClickListner=itemClickListner;
    }
}
