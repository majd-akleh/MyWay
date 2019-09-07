package Classes;

import android.graphics.Bitmap;


public class Wish {
    private String description;
    private Bitmap bitmap;

    //---magd--
    public int id ;
    public Wish(int id ) {
      this.id = id;
    }



    public Wish(Bitmap bitmap, String description) {
        this.bitmap = bitmap;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
