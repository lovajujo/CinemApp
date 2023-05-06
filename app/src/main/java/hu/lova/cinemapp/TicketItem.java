package hu.lova.cinemapp;

import java.util.Date;

public class TicketItem {
    private String id;
    private String title;
    private float rating;
    private int imageRes;
    private String price;
    private String date;
    private int cartCounter;

    public TicketItem(){}

    public TicketItem(String title, float rating, int imageRes, String price, String date, int cartCounter) {

        this.title = title;
        this.rating = rating;
        this.imageRes = imageRes;
        this.price=price;
        this.date=date;
        this.cartCounter=cartCounter;
    }

    public int getCartCounter() {
        return cartCounter;
    }

    public void setCartCounter(int cartCounter) {
        this.cartCounter = cartCounter;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public float getRating() {
        return rating;
    }

    public int getImageRes() {
        return imageRes;
    }
}
