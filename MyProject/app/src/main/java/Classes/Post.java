package Classes;


import android.graphics.Bitmap;

import java.util.Date;
import java.util.ArrayList;

public class Post {


    private int ID;
    private String username;
    private String description;
    private Bitmap image;
    private int likes;
    private int dislikes;
    private ArrayList<Comment> comments;
    private Date date;
    private int price;
    private Number rating;
    private String season ;

    public Post() {
    }

    public Post(int id , String username ,String description, Date date ) {
        ID = id ;
        this.username = username ;
        this.description = description;
        this.date = date;
        likes = 0 ;
        dislikes = 0 ;
        comments = new ArrayList<>();
    }

    public Post(int id , String username , String description , Bitmap image, String season, Number rating, int price, Date date) {
        this.ID= id;
        this.username = username ;
        this.season = season;
        this.rating = rating;
        this.price = price;
        this.date = date;
        this.image = image;
        this.description = description;
        likes = 0 ;
        dislikes = 0 ;
        comments = new ArrayList<>();
    }

    public Post(int id, String username, String description, Bitmap image, String season, Number rating, int price, Date date, int likes, int dislikes) {
        this.ID = id;
        this.username = username ;
        this.season = season;
        this.rating = rating;
        this.price = price;
        this.date = date;
        this.image = image;
        this.description = description;
        this.likes = likes ;
        this.dislikes = dislikes ;
        comments = new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public int getLikes() {
        return likes;
    }

    public Bitmap getImageName() {
        return image;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageName(Bitmap imageName) {
        this.image = image;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public Number getRating() {
        return rating;
    }

    public String getSeason() {
        return season;
    }

    public void setRating(Number rating) {
        this.rating = rating;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getID() {
        return ID;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
