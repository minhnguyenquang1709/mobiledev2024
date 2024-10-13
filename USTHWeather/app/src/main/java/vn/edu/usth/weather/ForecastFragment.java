package vn.edu.usth.weather;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForecastFragment extends Fragment {
    private AsyncTask<String, Integer, Bitmap> task;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ForecastFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForecastFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForecastFragment newInstance(String param1, String param2) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int[] days = {R.string.monday, R.string.tuesday, R.string.wednesday, R.string.thursday, R.string.friday, R.string.saturday, R.string.sunday, R.string.monday, R.string.tuesday, R.string.wednesday};
        int[] weatherStatuses = {R.string.partly_cloudy, R.string.showers, R.string.rain, R.string.scattered_showers, R.string.mostly_cloudy, R.string.partly_cloudy, R.string.thunderstorms, R.string.scattered_thunderstorms, R.string.showers, R.string.scattered_thunderstorms};
        int[] weatherIcons = {R.drawable.cloudy_sunny, R.drawable.showers, R.drawable.showers, R.drawable.cloudy_sunny_rainy, R.drawable.cloudy_sunny, R.drawable.cloudy_sunny, R.drawable.storm, R.drawable.cloudy_sunny_rainy, R.drawable.showers, R.drawable.cloudy_sunny_rainy};
        String[] temperatures = {"24C - 31C", "24C - 30C", "22C - 23C", "22C - 27C", "22C - 30C", "24C - 31C", "25C - 28C", "24C - 27C", "24C - 26C", "23C - 27C"};

        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        LinearLayout parentContainer = view.findViewById(R.id.weather_container);

        task = new MyAsyncTask();
        task.execute("https://usth.edu.vn/wp-content/uploads/2021/11/logo.png");

        for(int i = 0; i < days.length; i++){
            LinearLayout weatherForecastItem = createWeatherForecast();
            TextView tvDay = weatherForecastItem.findViewById(R.id.day);
            tvDay.setText(days[i]);

            ImageView iv = weatherForecastItem.findViewById(R.id.weather_icon);
            iv.setImageResource(weatherIcons[i]);

            TextView tvStatus = weatherForecastItem.findViewById(R.id.weather_status);
            tvStatus.setText(weatherStatuses[i]);

            TextView temperature = weatherForecastItem.findViewById(R.id.temperature);
            temperature.setText(temperatures[i]);

            parentContainer.addView(weatherForecastItem);
        }




        // Inflate the layout for this fragment
        return view;

        /**LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        TextView tv = new TextView(getContext());
        ImageView iv = new ImageView(getContext());

        ViewGroup.LayoutParams llParams = ll.getLayoutParams();
        llParams.width = MATCH_PARENT;
        llParams.height = MATCH_PARENT;
        ll.setLayoutParams(llParams);

        iv.setImageResource(R.drawable.storm);

        tv.setText("Thursday");

        ll.addView(tv);
        ll.addView(iv);

        return ll;*/
    }

    public LinearLayout createWeatherForecast(){
        LinearLayout weatherForecast = new LinearLayout(getContext());
        return (LinearLayout) getLayoutInflater().inflate(R.layout.weather_piece, weatherForecast);
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected void onPreExecute(){
            // nothing
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            // called by background thread
            Bitmap bitmap = null;
            try {
                Log.i("AsyncTask", "Started doing in background");
                // Thread.sleep(2000);

                // initialize URL
                URL url = new URL(strings[0]);

                // make a request to server
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);

                // allow reading response code and response data connection
                connection.connect();

                // receive response
                int response = connection.getResponseCode();
                Log.i("AsyncTask", "The response code is " + response);
                InputStream is = connection.getInputStream();

                // process image response
                bitmap = BitmapFactory.decodeStream(is);

                connection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // This method is called in the main thread, so it's possible
            // to update UI to reflect the worker thread progress here.

            // update progress bar

        }

        @Override
        protected void onPostExecute(Bitmap result){
            // called in the main thread, after doInBackground()
            try {
                if (result != null){
                    ImageView logo = getView().findViewById(R.id.logo);
                    logo.setImageBitmap(result);
                    Toast.makeText(getContext(), "data received", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(), "cannot receive data", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}