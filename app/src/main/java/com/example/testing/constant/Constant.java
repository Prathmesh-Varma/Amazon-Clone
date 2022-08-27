package com.example.testing.constant;

import com.example.testing.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Constant {
    public static final List<Integer>QUANTITY_LIST=new ArrayList<>();
    static {
        for (int i = 1; i < 11; i++) {
            QUANTITY_LIST.add(i);
        }
    }

    public static final Product PRODUCT1=new Product(1,"   Redme note11T", BigDecimal.valueOf(16999),
            "Bohot acha hai","redme");

    public static final Product PRODUCT2=new Product(2,"   One plus", BigDecimal.valueOf(24999),
            "le lo yaar jaldi","oneplus");
    public static final Product PRODUCT3=new Product(3,"    Samsung",BigDecimal.valueOf(7999),
            "acha lga to lelo", "samsung");

    public static final List<Product> PRODUCT_LIST=new ArrayList<Product>();
    static {
        PRODUCT_LIST.add(PRODUCT1);
        PRODUCT_LIST.add(PRODUCT2);
        PRODUCT_LIST.add(PRODUCT3);
    }

    public static final String CURRENCY="₹";
}
