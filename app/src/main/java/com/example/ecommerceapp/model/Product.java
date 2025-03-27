package com.example.ecommerceapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private int id;
    private String name;
    private String description;
    private double price;
    private int categoryId;
    private String imageUrl;
    private int stock;

    public Product(int id, String name, String description, double price, int categoryId, String imageUrl, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    protected Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        categoryId = in.readInt();
        imageUrl = in.readString();
        stock = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeInt(categoryId);
        dest.writeString(imageUrl);
        dest.writeInt(stock);
    }
}
