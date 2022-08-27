package com.example.testing.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testing.R;
import com.example.testing.interfaces.ItemClickListner;

public class RelatedProductsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView relatedProdName,relatedProdPrice;
    public ImageView relatedProdImage;
    private ItemClickListner itemClickListner;
    public RelatedProductsHolder(@NonNull View itemView) {

        super(itemView);
        relatedProdName=itemView.findViewById(R.id.relatedProdName);
        relatedProdPrice=itemView.findViewById(R.id.relatedProdPrice);
        relatedProdImage=itemView.findViewById(R.id.relatedProdImg);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view,getAdapterPosition(),false);
    }
}
