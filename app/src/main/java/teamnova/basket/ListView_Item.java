package teamnova.basket;

import android.widget.ImageView;

public class ListView_Item {
    String title;
    String conten;
    String address;
    String image;

    public void setTitle(String Title){
        title = Title;
    }
    public void setConten(String Conten){
        conten = Conten;
    }
    public void setAddress(String Address){
        address = Address;
    }
    public void setImage(String Image){
        image = Image;
    }

    public String getTitle(){
        return title;
    }
    public String getConten(){
        return conten;
    }
    public String getAddress(){return address;}
    public String getImage(){
        return image;
    }
}
