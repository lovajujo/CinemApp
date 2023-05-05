package hu.lova.cinemapp;

public class TicketItem {
    private String id;
    private String name;
    private String itemType;
    private String price;
    private float rating;
    private int imageRes;
    private int cartCounter;

    public TicketItem(){}

    public TicketItem(String name, String itemType, String price, float rating, int imageRes, int cartCounter) {
        this.name = name;
        this.itemType = itemType;
        this.price = price;
        this.rating = rating;
        this.imageRes = imageRes;
        this.cartCounter = cartCounter;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String _getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getItemType() {
        return itemType;
    }

    public String getPrice() {
        return price;
    }

    public float getRating() {
        return rating;
    }

    public int getImageRes() {
        return imageRes;
    }

    public int getCartCounter() {
        return cartCounter;
    }
}
