package teamnova.basket;

/**
<<<<<<< HEAD
 * Created by jwh48 on 2017-02-11.
=======
 * Created by 1218m on 2017-02-11.
>>>>>>> 81b0fc0f06cec3e78ac5db0bd6ca709eefa2abc1
 */

public class UrlDetectionUtil {

    public static String getURL(String message) {
        // TODO Auto-generated method stub

        String[] str = message.split(" ");
        String response = "not URL";

        for (int i = 0; i < str.length; i++) {
            if (getHttpStartPoint(str[i]) != -1) {
                response = str[i].substring(getHttpStartPoint(str[i]));
            }
        }

        return response;

    }

    public static int getHttpStartPoint(String str) {
        // TODO Auto-generated method stub
        if (str.length() - 4 <= 0) {
            return -1;
        }
        for (int i = 0; i < str.length() - 4; i++) {
            if (str.substring(i, i + 4).equals("http")) {
                return i;
            }
        }
        return -1;
    }
}
