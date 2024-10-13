package vn.edu.usth.weather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {
    private final String tag = "Weather";
    private MediaPlayer mp;
    private Toolbar appbar;
    private final Handler handler;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather);
        // -----------------------------------------



        appbar = findViewById(R.id.toolbar);
        appbar.setBackgroundColor(getResources().getColor(R.color.blue_actionBar));
        setSupportActionBar(appbar);

        Toast.makeText(getApplicationContext(), appbar.getTitle(), Toast.LENGTH_SHORT).show();

        try {
            InputStream is = getResources().openRawResource(R.raw.erikmusic);

            File sdCard = Environment.getExternalStorageDirectory();
            File musicFile = new File(sdCard, "erikmusic.mp3");

            OutputStream outputStream = new FileOutputStream(musicFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            is.close();
            outputStream.close();

            mp = new MediaPlayer();
            mp.setDataSource(musicFile.getAbsolutePath());
            mp.prepare();
            mp.start();
        }catch (Exception e){
            e.printStackTrace();
        }

        // setup ViewPager
        PagerAdapter adapter = new HomeFragmentPagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.myViewPager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(adapter);

        // setup link between TabLayout and ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(pager);

        requestQueue = Volley.newRequestQueue(this);

        // change bar color

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public WeatherActivity(){
        super();
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg){
                if (msg != null){
                    Log.i("Handler", msg.toString());
                    Toast.makeText( getApplicationContext(),msg.getData().getString(getString(R.string.server_response)), Toast.LENGTH_SHORT).show();
                }

            }
        };
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(tag, "started");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(tag, "resumed");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(tag, "paused");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(tag, "stopped");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(tag, "destroyed");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.action_refresh){
            // Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT).show();
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try{
//                        Thread.sleep(5000);
//                    }catch(InterruptedException e){
//                        e.printStackTrace();
//                    }
//                    // simulate receiving data from server
//                    Bundle bundle = new Bundle();
//                    bundle.putString(getString(R.string.server_response), "Data from server");
//
//                    // notify main thread
//                    Message msg = new Message();
//                    msg.setData(bundle);
//                    handler.sendMessage(msg);
//                }
//            });
//            t.start();

            // task = new MyAsyncTask();
            // task.execute("http://ict.usth.edu.vn/wp-content/uploads/usth/usthlogo.png");

            return true;
        }else if (itemId == R.id.action_settings){
            Toast.makeText(this, "Settings hehe", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}