package com.example.testing.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testing.R;
import com.example.testing.interfaces.ItemClickListner;

public class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView orderName,orderAdr,orderCity,orderDate,orderPrice,orderPhone;
    private ItemClickListner itemClickListner;

    public OrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        orderName=itemView.findViewById(R.id.orderName);
        orderAdr=itemView.findViewById(R.id.orderAdr);
        orderCity=itemView.findViewById(R.id.orderCity);
        orderPhone=itemView.findViewById(R.id.orderPhone);
        orderPrice=itemView.findViewById(R.id.oddshoeprice);
        orderDate=itemView.findViewById(R.id.orderDate);

    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
