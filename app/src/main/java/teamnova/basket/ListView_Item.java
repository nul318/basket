package teamnova.basket;

import android.widget.ImageView;

public class ListView_Item {
    String title;
    String conten;
    ImageView image;

    public void setTitle(String Title){
        title = Title;
    }
    public void setConten(String Conten){
        conten = Conten;
    }
    public void setImage(ImageView Image){
        image = Image;
    }
    public String getTitle(){
        return title;
    }
    public String getConten(){
        return conten;
    }
    public ImageView getImage(){
        return image;
    }
}
