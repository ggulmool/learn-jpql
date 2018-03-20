package me.ggulmool.entity;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "PRODUCT_ID")
    private Long id;
    private String name;
    private Integer price;
    @Column(name = "STOCKAMOUNT")
    private Integer stockAmount;

    protected Product() {
    }

    public Product(String name, Integer price, Integer stockAmount) {
        this.name = name;
        this.price = price;
        this.stockAmount = stockAmount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getStockAmount() {
        return stockAmount;
    }

    @Override
    public String toString() {
        return String.format("Product[%s %d %d]", name, price, stockAmount);
    }
}
