package com.shells.percolatecoffee.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.shells.percolatecoffee.BuildConfig;
import com.shells.percolatecoffee.R;
import com.shells.percolatecoffee.android.views.CoffeeDetailFragment;
import com.shells.percolatecoffee.api.MemberClient;
import com.shells.percolatecoffee.api.models.CoffeeResource;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CoffeeDetailActivity extends ActionBarActivity implements CoffeeDetailFragment.OnFragmentInteractionListener {

    public final static String ID = "param_id";
    String id;
    Activity activity;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        id = getIntent().getStringExtra(ID);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        init();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, CoffeeDetailFragment.newInstance(id))
                    .commit();
        }

    }

    private void init(){
        setToolbar();
        getCoffee(id);
    }

    private void setToolbar() {
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getCoffee(String id) {
        MemberClient.getApiClient().getCoffee(BuildConfig.API_TOKEN, id, new Callback<CoffeeResource>() {
            @Override
            public void success(CoffeeResource resource, Response response) {
                if ((getSupportFragmentManager().findFragmentById(R.id.container)) != null && getSupportFragmentManager().findFragmentById(R.id.container).isVisible()) {
                    ((CoffeeDetailFragment) getSupportFragmentManager().findFragmentById(R.id.container)).onCoffeeLoaded(activity, resource);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("CoffeeDetailActivity", error.getMessage());
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCoffeeSelected(String id) {

    }
}
