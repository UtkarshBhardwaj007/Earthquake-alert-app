package com.example.earthquakealert;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity /*implements LoaderManager.LoaderCallbacks<ArrayList<Earthquake>>*/ {

    private static final String LOG_TAG = MainActivity.class.getName();
    // URL for earthquake data from the USGS data set
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";
    /*
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    //̥private static final int EARTHQUAKE_LOADER_ID = 1;

    private EarthquakeAdapter adapter;

    /* TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    private ConnectivityManager connMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a fake list of earthquake locations.
        //ArrayList<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(USGS_REQUEST_URL);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new EarthquakeAdapter(
                this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Earthquake currentEarthquake = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                assert currentEarthquake != null;
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Start the AsyncTask to fetch the earthquake data

        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);


        // Get a reference to the LoaderManager, in order to interact with loaders.
        //LoaderManager loaderManager = getSupportLoaderManager();
        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        //loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
    }

    /*@NonNull
    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int id, @Nullable Bundle args) {
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> data) {
        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Earthquake>> loader) {
        adapter.clear();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the list of earthquakes in the response.
     *
     * AsyncTask has three generic parameters: the input type, a type used for progress updates, and
     * an output type. Our task will take a String URL, and return an Earthquake. We won't do
     * progress updates, so the second generic is just Void.
     *
     * We'll only override two of the methods of AsyncTask: doInBackground() and onPostExecute().
     * The doInBackground() method runs on a background thread, so it can run long-running code
     * (like network activity), without interfering with the responsiveness of the app.
     * Then onPostExecute() is passed the result of doInBackground() method, but runs on the
     * UI thread, so it can use the produced data to update the UI.
     */
    private class EarthquakeAsyncTask extends AsyncTask<String, Void, ArrayList<Earthquake>> {

        //* This method runs on a background thread and performs the network request.
        //* We should not update the UI from a background thread, so we return a list of
        //* {@link Earthquake}s as the result.

        @Override
        protected ArrayList<Earthquake> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            return QueryUtils.fetchEarthquakeData(urls[0]);
        }

        //* This method runs on the main UI thread after the background work has been
        //* completed. This method receives as input, the return value from the doInBackground()
        //* method. First we clear out the adapter, to get rid of earthquake data from a previous
        //* query to USGS. Then we update the adapter with the new list of earthquakes,
        //* which will trigger the ListView to re-populate its list items.
        @Override
        protected void onPostExecute(ArrayList<Earthquake> data) {
            // Hide loading indicator because the data has been loaded
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Get details on the currently active default data network
            // because this activity implements the LoaderCallbacks interface).
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            // If there is a network connection, fetch data
            if (networkInfo != null && networkInfo.isConnected()) {
                mEmptyStateTextView.setText(R.string.no_earthquakes);
                // Clear the adapter of previous earthquake data
                adapter.clear();
                // If there is a valid Arraylist of {@link Earthquake}s, then add them to the adapter's
                // data set. This will trigger the ListView to update.
                if (data != null && !data.isEmpty()) {
                    adapter.addAll(data);
                }
            }
            else{
                mEmptyStateTextView.setText(R.string.no_internet_connection);
            }
        }
    }
}