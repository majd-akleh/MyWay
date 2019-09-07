package Classes;


import java.util.Date;

public class Comment {

    private String commenter;
    private String description;
    private Date date;

    public Comment(String commenter,String description, Date date) {
        this.commenter = commenter;
        this.description = description;
        this.date = date;
    }

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
