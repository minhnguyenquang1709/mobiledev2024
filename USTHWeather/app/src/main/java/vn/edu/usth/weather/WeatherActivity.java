package vn.edu.usth.weather;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class WeatherActivity extends AppCompatActivity {
    private final String tag = "Weather";
    private MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setTheme(androidx.appcompat.R.style.Theme_AppCompat_DayNight);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather);

        try {
            InputStream is = getResources().openRawResource(R.raw.erikmusic);

            File sdCard = Environment.getExternalStorageDirectory();
            File musicFile = new File(sdCard, "my_music.mp3");

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

        // ForecastFragment forecastFragment = new ForecastFragment();

        // Add the fragment to the 'container' FrameLayout
        // getSupportFragmentManager().beginTransaction().add(R.id.mainContainer, forecastFragment).commit();

        // change bar color


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public WeatherActivity(){
        super();
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
}