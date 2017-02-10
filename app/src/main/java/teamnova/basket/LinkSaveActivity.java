package teamnova.basket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by jwh48 on 2017-02-11.
 */

public class LinkSaveActivity extends Activity {

    String user_id;
    String[] folder_name;
    int selected_folder_id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_link_save);

        loadUserId();

        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                showFolderSelectDialog(folder_name);
            }
        };

        Log.d("LinkSave", "user id : " + user_id);
        Intent intent = getIntent();
        String link = intent.getStringExtra("link_data");
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("Basket")
                .setMessage(UrlDetectionUtil.getURL(link) + "을 담으시겠습니까?")
                .setPositiveButton("담기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HttpUtil folder_request = new HttpUtil();
                        folder_request.setUrl("http://52.79.189.195/nova-thon/basket/folder/select.php")
                                .setMethod(HttpUtil.HTTP_METHOD_POST)
                                .setData("user_id", user_id)
                                .setCallback(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String result = response.body().string();
                                        try {
                                            JSONArray folder_data_array = new JSONArray(result);
                                            folder_name = new String[folder_data_array.length()];
                                            for(int i = 0; i < folder_data_array.length(); i++)
                                            {
                                                JSONObject folder_data = folder_data_array.getJSONObject(i);
                                                folder_name[i] = folder_data.getString("folder");
                                                Log.d("LinkSave", "folder name : " + folder_data.getString("folder"));
                                            }
                                            handler.sendEmptyMessage(0);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("LinkSave", "folder response : " + result);
                                    }
                                })
                                .execute();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        LinkSaveActivity.this.finish();
                    }
                });
        AlertDialog dialog = build.create();
        dialog.show();
    }

    void loadUserId()
    {
        SharedPreferences user_info = getSharedPreferences("user_info", MODE_PRIVATE);
        this.user_id = user_info.getString("id", null);
    }

    void showFolderSelectDialog(final String[] folderName)
    {
        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(getApplicationContext(), "저장되었습니다", Toast.LENGTH_LONG).show();
            }
        };


        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("폴더 선택")
                .setSingleChoiceItems(folderName, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selected_folder_id = i;
                    }
                })
                .setNeutralButton("담기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        HttpUtil insert_request = new HttpUtil();
                        insert_request.setMethod(HttpUtil.HTTP_METHOD_POST)
                                .setUrl("http://52.79.189.195/nova-thon/basket/data/insert.php")
                                .setData("user_id", user_id)
                                .setData("folder", folderName[selected_folder_id])
                                .setCallback(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        Log.d("LinkSave", "insert response : " + response.toString());
                                        handler.sendEmptyMessage(0);
                                        dialogInterface.dismiss();
                                        LinkSaveActivity.this.finish();
                                    }
                                })
                                .execute();

                    }
                });
        AlertDialog dialog = build.create();
        dialog.show();
    }

}
