package teamnova.basket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView listView;
    ListView_Adapter adapter = new ListView_Adapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String user_id = "A";
        String folder = "전체";
        String uri;

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if(Intent.ACTION_SEND.equals(action) && type != null){
            if("text/plain".equals(type)){
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);

                uri = sharedText;
                uri=UrlDetectionUtil.getURL(uri);
                Toast.makeText(this, uri, Toast.LENGTH_SHORT).show();
                HttpUtil httpUtil = new HttpUtil();
                httpUtil.setUrl("http://52.79.189.195/nova-thon/basket/data/insert.php")
                        .setCallback(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                Log.e("RESPONSE", "dasadda");
                                Log.e("RESPONSE", "dasadda");
                                Log.e("RESPONSE", "dasadda");
                                Log.e("RESPONSE", "dasadda");
                                Log.e("RESPONSE", "dasadda");
                                Log.e("RESPONSE", "dasadda");
                                Log.e("RESPONSE", "dasadda");

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                        Log.e("RESPONSE", response.body().string());
                                Log.e("RESPONSE", response.body().string());
                                Log.e("RESPONSE", response.body().string());
                                Log.e("RESPONSE", response.body().string());

                            }
                        })
                        .setMethod(HttpUtil.HTTP_METHOD_POST)
                        .setData("user_id", user_id)
                        .setData("folder", folder)
                        .setData("url", uri)
                        .execute();

            }
        }



        final Handler handler = new Handler();

        HttpUtil httpUtil = new HttpUtil();
        httpUtil.setUrl("http://52.79.189.195/nova-thon/basket/data/select.php")
                .setCallback(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
//                        Log.e("RESPONSE", response.body().string());
                        try {
                            JSONArray jarray = new JSONArray(response.body().string());
                            for(int i=0; i < jarray.length(); i++){
                                final JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                                jObject.getString("contents");
                                jObject.getString("image_path");
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            adapter.addItem(jObject.getString("title"),jObject.getString("contents"),jObject.getString("url"),jObject.getString("image_path") );
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .setMethod(HttpUtil.HTTP_METHOD_POST)
                .setData("user_id", user_id)
                .setData("folder", folder)
                .execute();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        listView =  (ListView) findViewById(R.id.main_Listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.removeItem(position);
                adapter.notifyDataSetChanged();
            }
        });

        this.setTitle("");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_serach) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onResume(){
        super.onResume();

        adapter.notifyDataSetChanged();

    }
}
