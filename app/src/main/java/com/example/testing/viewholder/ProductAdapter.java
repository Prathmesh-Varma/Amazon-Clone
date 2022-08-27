package com.example.testing.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testing.R;
import com.example.testing.constant.Constant;
import com.example.testing.model.Product;
import android.content.Context;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends BaseAdapter {

    private List<Product> products=new ArrayList<Product>();

    private final Context context;

    public ProductAdapter(Context context){
        this.context=context;
    }

    public void updateProduct(List<Product> products){
        this.products.addAll(products);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        TextView productName, productprice;
        ImageView productImage;

        if (view==null){
            view= LayoutInflater.from(context).inflate(R.layout.activity_product_adapter, parent,false);
            productImage=view.findViewById(R.id.productImage);
            productprice=view.findViewById(R.id.productPrice);
            productName=view.findViewById(R.id.productName);
            view.setTag(new ViewHolder(productName,productprice,productImage));
        }else {
            ViewHolder viewholder=(ViewHolder) view.getTag();
            productName=viewholder.productName;
            productprice=viewholder.productprice;
            productImage=viewholder.productImage;

        }

        final Product product=getItem(i);
        productName.setText(product.getpName());
        productprice.setText(Constant.CURRENCY+String.valueOf(product.getpPrice().setScale(0, BigDecimal.ROUND_HALF_DOWN)));
        productImage.setImageResource(context.getResources().getIdentifier(product.getpImagename(),"drawable",context.getPackageName()));
        return view;

    }

    private static class ViewHolder{
        public final TextView productName;
        public final TextView productprice;
        public final ImageView productImage;

        public ViewHolder(TextView productName,TextView productprice,ImageView productImage){
            this.productName=productName;
            this.productprice=productprice;
            this.productImage=productImage;
        }
    }
}
