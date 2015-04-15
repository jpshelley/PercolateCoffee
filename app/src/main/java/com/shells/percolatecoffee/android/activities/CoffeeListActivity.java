package com.shells.percolatecoffee.android.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.shells.percolatecoffee.R;
import com.shells.percolatecoffee.android.views.CoffeeListFragment;

public class CoffeeListActivity extends ActionBarActivity implements CoffeeListFragment.OnFragmentInteractionListener {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        init();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CoffeeListFragment())
                    .commit();
        }

    }

    private void init(){
        setToolbar();
    }

    private void setToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCoffeeSelected(String id) {
        Intent intent = new Intent(this, CoffeeDetailActivity.class);
        intent.putExtra(CoffeeDetailActivity.ID, id);
        startActivity(intent);
    }
}
