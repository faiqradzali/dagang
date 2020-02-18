/*
 * This is a simple and easy approach to reuse the same
 * navigation drawer on your other activities. Just create
 * a base layout that conains a DrawerLayout, the
 * navigation drawer and a FrameLayout to hold your
 * content view. All you have to do is to extend your
 * activities from this class to set that navigation
 * drawer. Happy hacking :)
 * P.S: You don't need to declare this Activity in the
 * AndroidManifest.xml. This is just a base class.
 */
package com.example.myapplication;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;


public abstract class BaseActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {
    private FrameLayout view_stub; //This is the framelayout to keep your content view
    private NavigationView navigation_view; // The new navigation view from Android Design Library. Can inflate menu resources. Easy
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Menu drawerMenu;
    HashMap<String, String> user;
    Toolbar toolbar;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);// The base layout that contains your navigation drawer.
        view_stub = (FrameLayout) findViewById(R.id.view_stub);
        navigation_view = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerMenu = navigation_view.getMenu();
        for(int i = 0; i < drawerMenu.size(); i++) {
            drawerMenu.getItem(i).setOnMenuItemClickListener(this);
        }
        // and so on...
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /* Override all setContentView methods to put the content view to the FrameLayout view_stub
     * so that, we can make other activity implementations looks like normal activity subclasses.
     */
    @Override
    public void setContentView(int layoutResID) {
        if (view_stub != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View stubView = inflater.inflate(layoutResID, view_stub, false);
            view_stub.addView(stubView, lp);
        }
    }

    @Override
    public void setContentView(View view) {
        if (view_stub != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            view_stub.addView(view, lp);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (view_stub != null) {
            view_stub.addView(view, params);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetail();
        switch (item.getItemId()) {
            case R.id.nav_dashboard:
                Intent intent1 = new Intent(this, Dashboard.class);
                this.startActivity(intent1);
                break;

            case R.id.nav_screeners:
                Intent intent2 = new Intent(this,ScreenerlistActivity.class);
                this.startActivity(intent2);
                break;
            case R.id.nav_stocklist:
                Intent intent3 = new Intent(this,StocklistActivity.class);
                this.startActivity(intent3);
                break;
            case R.id.nav_log:
                Intent intent4 = new Intent(this,LogActivity.class);
                this.startActivity(intent4);// do whatever
                break;
            case R.id.nav_portfolio:
                Intent intent5 = new Intent(this,PortfolioActivity.class);
                this.startActivity(intent5);// do whatever
                break;
            case R.id.nav_opencds:
                Intent intent6 = new Intent(this,OpenAccountActivity.class);
                this.startActivity(intent6);// do whatever
                break;
            case R.id.nav_logout:
                Intent intent7 = new Intent(this, Dashboard.class);
                this.startActivity(intent7);
                sessionManager.logout();
                break;

            // and so on...
        }
        return true;
    }
}
