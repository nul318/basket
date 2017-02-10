package teamnova.basket;
import android.os.AsyncTask;
import java.io.File;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {
    static MultipartBody.Builder request_body;
    static String url;
    static int method;
    static okhttp3.Callback callback;

    static final int HTTP_METHOD_POST = 0;
    static final int HTTP_METHOD_GET = 1;
    static final int HTTP_METHOD_PUT = 2;
    static final int HTTP_METHOD_DELETE = 3;
    HttpUtil(){
        request_body = new MultipartBody.Builder().setType(MultipartBody.FORM);

    }
    HttpUtil setFile(String key, String value, String path) {
        request_body.addFormDataPart(key, value, RequestBody.create(MultipartBody.FORM, new File(path)));
        return this;
    }
    HttpUtil setData(String key, String value) {
        request_body.addFormDataPart(key, value);
        return this;
    }
    HttpUtil setCallback(okhttp3.Callback callback) {
        HttpUtil.callback = callback;
        return this;
    }
    HttpUtil setUrl(String url) {
        HttpUtil.url = url;
        return this;
    }
    HttpUtil setMethod(int method) {
        HttpUtil.method = method;
        return this;
    }
    HttpUtil execute() {
        new Execute().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return this;
    }
    private static class Execute extends AsyncTask<Object, Object, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            Request request;
            switch(method){
                case HTTP_METHOD_POST :
                    request = new Request.Builder()
                            .url(url)
                            .post(request_body.build())
                            .build();
                    break;
                case HTTP_METHOD_GET :
                    request = new Request.Builder()
                            .url(url)
                            .get()
                            .build();
                    break;
                case HTTP_METHOD_PUT :
                    request = new Request.Builder()
                            .url(url)
                            .put(request_body.build())
                            .build();
                    break;
                case HTTP_METHOD_DELETE :
                    request = new Request.Builder()
                            .url(url)
                            .delete(request_body.build())
                            .build();
                    break;
                default:
                    request = new Request.Builder()
                            .url(url)
                            .post(request_body.build())
                            .build();
                    break;
            }
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(callback);
            return null;
        }
    }
    /*
     * 사용예
     */
//    HttpUtil httpUtil = new HttpUtil();
//    httpUtil.setUrl("http://pushit.co.kr/members/36961167")
//            .setCallback(new Callback() {
//        @Override
//        public void onFailure(Call call, IOException e) {
//
//        }
//        @Override
//        public void onResponse(Call call, Response response) throws IOException {
//            Log.e("RESPONSE" , response.body().string());
//        }
//    })
//            .setMethod(HttpUtil.HTTP_METHOD_POST)
//    .setData("company" , name.getText().toString())
//            .setData("company_address" , birth.getText().toString())
//            .setData("company_rank" , name.getText().toString())
//            .setData("phone_number" , birth.getText().toString())
//            .execute();
}