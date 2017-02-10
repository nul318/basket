package teamnova.basket;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static String folder;

    ListView listView;
    ListView_Adapter adapter = new ListView_Adapter();
    ListView_nav_Adapter adapter_nav = new ListView_nav_Adapter();

    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user_id = "A";


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


        ListView nav_list_view = (ListView) findViewById(R.id.nav_list);

        nav_list_view.setAdapter(adapter_nav);

        final Handler handler = new Handler();

        HttpUtil httpUtil = new HttpUtil();
        httpUtil.setUrl("http://52.79.189.195/nova-thon/basket/folder/select.php")
                .setCallback(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
//                        Log.e("RESPONSE", response.body().string());
                        try {
                            JSONArray jarray = new JSONArray(response.body().string());
                            for(int i=0; i < jarray.length(); i++){
                                final JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            adapter_nav.addItem(jObject.getString("folder") );
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        adapter_nav.notifyDataSetChanged();
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
                .execute();



        nav_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                //Toast.makeText(getApplicationContext(), arDessert.get(position), Toast.LENGTH_SHORT).show();
                folder = adapter_nav.getItem(position).toString();

            }

        });


        Button folder_add = (Button) findViewById(R.id.folder_add);
        folder_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout titleLayout = (LinearLayout) vi.inflate(R.layout.folder_add, null);
                final EditText et = (EditText) titleLayout.findViewById(R.id.folder_title);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("폴더 명")
                        .setView(titleLayout)
                        .setNeutralButton("확인", new DialogInterface.OnClickListener() { //확인버튼

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub


                                HttpUtil httpUtil = new HttpUtil();
                                httpUtil.setUrl("http://52.79.189.195/nova-thon/basket/folder/insert.php")
                                        .setCallback(new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {

                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                Log.e("RESPONSE", response.body().string());
                                                Log.e("TAG", String.valueOf(adapter_nav.getCount()));
//                                                for(int i=0; i<adapter.getCount(); i++) {
//                                                    adapter.removeItem(i);
//                                                }
//                                                for(int i=0; i<adapter_nav.getCount(); i++) {
//                                                    adapter_nav.removeItem(i);
//                                                }
                                                 adapter.clear();
                                                adapter_nav.clear();
                                                Log.e("TAG", String.valueOf(adapter_nav.getCount()));


                                                HttpUtil httpUtil = new HttpUtil();
                                                httpUtil.setUrl("http://52.79.189.195/nova-thon/basket/folder/select.php")
                                                        .setCallback(new Callback() {
                                                            @Override
                                                            public void onFailure(Call call, IOException e) {

                                                            }

                                                            @Override
                                                            public void onResponse(Call call, Response response) throws IOException {
//                        Log.e("RESPONSE", response.body().string());
                                                                try {
                                                                    JSONArray jarray = new JSONArray(response.body().string());
                                                                    for(int i=0; i < jarray.length(); i++){
                                                                        final JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                                                                        handler.post(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                try {
                                                                                    adapter_nav.addItem(jObject.getString("folder") );
                                                                                } catch (JSONException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                                adapter_nav.notifyDataSetChanged();
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
                                                        .execute();
                                            }
                                        })
                                        .setMethod(HttpUtil.HTTP_METHOD_POST)
                                        .setData("user_id", user_id)
                                        .setData("folder", et.getText().toString())
                                        .execute();

                            }
                        })
                        .show();
            }
        });










        listView =  (ListView) findViewById(R.id.main_Listview);
        listView.setAdapter(adapter);

        adapter.addItem("여행갈 때 차에서 들려주는 동요 상어 가족 외 43곡 모음집 핑크퐁! 인기동요","youtube.com");
        adapter.addItem("hello","hello");
        adapter.notifyDataSetChanged();

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
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
