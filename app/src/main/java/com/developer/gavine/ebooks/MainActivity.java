package com.developer.gavine.ebooks;

import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Ebooks>> {
    private EbooksAdapter mAdapter;
    TextView mEmptyStateTextView;
    private SwipeRefreshLayout swipeContainer;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final String SAMPLE_JSON_RESPONSE = "https://got-quotes.herokuapp.com/quotes";
    ListView ebooksListView;
    public Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (android.os.Build.VERSION.SDK_INT >= 21){
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusBar));
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);}


        // Find a reference to the {@link ListView} in the layout
        ebooksListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new EbooksAdapter(this, new ArrayList<Ebooks>());
        ebooksListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        ebooksListView.setEmptyView(mEmptyStateTextView);

            loader();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ebooksListView.setAdapter(mAdapter);
                ebooksListView.setEmptyView(mEmptyStateTextView);
                loader();
            }
        });

    }

    public void loader()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if(ni != null && ni.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null,this);

        }
        else
        {
            View loadingIndicator = findViewById(R.id.progress);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }



    @Override
    public Loader<List<Ebooks>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new EbookAsyncTaskLoader(this, SAMPLE_JSON_RESPONSE);
    }
    @Override
    public void onLoadFinished(Loader<List<Ebooks>> loader, List<Ebooks> earthquakes) {

        View loadingIndicator = findViewById(R.id.progress);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.not_found);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();



        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()){
            mAdapter.addAll(earthquakes);
            swipeContainer.setRefreshing(false);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<Ebooks>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            startActivity(new Intent(MainActivity.this,About.class));
        }
        if (id == R.id.action_instructions) {
            startActivity(new Intent(MainActivity.this,Instructions.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new android.app.AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("no", null).show();
    }
}
