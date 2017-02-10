package teamnova.basket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by jwh48 on 2017-02-11.
 */

public class LoginActivity extends AppCompatActivity {

    final String TAG = "Login";

    CallbackManager callbackManager;
    AccessToken access_token;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginButton login_button = (LoginButton)findViewById(R.id.login_button);
        login_button.setReadPermissions("email", "public_profile" );
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                access_token = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(access_token, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        final JSONObject facebook_user_data = response.getJSONObject();
                        Log.d(TAG, "login json data : " + response.getJSONObject());

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

                        HttpUtil http_request = new HttpUtil();
                        try {
                            String image_path = "https://graph.facebook.com/" + facebook_user_data.getString("id") + "/picture";

                            http_request.setUrl("http://52.79.189.195/nova-thon/basket/login.php")
                                    .setCallback(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {

                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            Log.d(TAG, "response : " + response.toString());

                                            if(response.code() == 200)
                                            {
                                                Intent main_activity = new Intent(LoginActivity.this, MainActivity.class);
                                                SharedPreferences user_info = getSharedPreferences("user_info", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = user_info.edit();
                                                try {
                                                    editor.putString("id", facebook_user_data.getString("id"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                editor.commit();
                                                startActivity(main_activity);
                                            }
                                        }
                                    })
                                    .setMethod(HttpUtil.HTTP_METHOD_POST)
                                    .setData("user_id", facebook_user_data.getString("id"))
                                    .setData("email", facebook_user_data.getString("email"))
                                    .setData("name", facebook_user_data.getString("name"))
                                    .setData("image_path", image_path)
                                    .execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                Bundle params = new Bundle();
                params.putString("fields", "email, name, id");
                request.setParameters(params);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "로그인 취소", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "로그인 에러", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
