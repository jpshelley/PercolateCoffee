package com.shells.percolatecoffee.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
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
    private ShareActionProvider mShareActionProvider;

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
            getSupportActionBar().setTitle("");
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
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        // Set up ShareActionProvider's default share intent
        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        mShareActionProvider.setShareIntent(getDefaultIntent());

        return super.onCreateOptionsMenu(menu);
    }

    /** Defines a default (dummy) share intent to initialize the action provider.
     * However, as soon as the actual content to be used in the intent
     * is known or changes, you must update the share intent by again calling
     * mShareActionProvider.setShareIntent()
     */
    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);    }

    @Override
    public void onCoffeeSelected(String id) {

    }
}
