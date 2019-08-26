package com.intutrack.intudock.UI;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import com.intutrack.intudock.Models.NavigationModel;
import com.intutrack.intudock.Preference.PrefEntities;
import com.intutrack.intudock.Preference.Preferences;
import com.intutrack.intudock.R;
import com.intutrack.intudock.UI.Adapters.NavigationAdapter;
import com.intutrack.intudock.UI.Fragments.HomeFragment;
import com.intutrack.intudock.UI.Fragments.WarehouseFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavigationAdapter.NavigationAdapterCallback {

    private FrameLayout frameLayout;
    private Toolbar toolbar;
    private NavigationAdapter adapter;
    private NavigationModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        RecyclerView recyclerViewMenu =  header.findViewById(R.id.rv_menu);
        TextView clientName = header.findViewById(R.id.tv_client_name);
        TextView loginId = header.findViewById(R.id.tv_login_id);

        clientName.setText(Preferences.getPreference(this, PrefEntities.CLIENT));
        loginId.setText(Preferences.getPreference(this, PrefEntities.EMAIL));

        addMenu(recyclerViewMenu);

        frameLayout = findViewById(R.id.framelayout);

        displaySelectedScreen(0);

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

//        displaySelectedScreen(item.getItemId());

        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case 0:
                toolbar.setTitle("Home");
                fragment = new HomeFragment();
                break;
            case 1:
                toolbar.setTitle("Warehouse(s)");
                fragment = new WarehouseFragment();
                break;
            case 2:
                requestLogout();
                break;
            default:
                toolbar.setTitle("Home");
                fragment = new HomeFragment();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.framelayout, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void requestLogout() {

        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_logout);

        Button yes = dialog.findViewById(R.id.btn_yes);
        Button no = dialog.findViewById(R.id.btn_no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Preferences.removeAllPreference(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                dialog.dismiss();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               dialog.dismiss();

            }
        });

        dialog.show();

    }

    public void addMenu(RecyclerView recyclerViewMenu){

        model = new NavigationModel();
        ArrayList<NavigationModel> menuList = new ArrayList<>();

        model.setTitle("Home");
        model.setText("Dashboard");
        model.setIcon(R.drawable.icon_home);

        menuList.add(model);

        NavigationModel model2 = new NavigationModel();
        model2.setTitle("Warehouse(s)");
        model2.setText("Schedule your transactions");
        model2.setIcon(R.drawable.icon_warehouse);

        menuList.add(model2);

        NavigationModel model3 = new NavigationModel();
        model3.setTitle("Logout");
        model3.setText("Logout of the app");
        model3.setIcon(R.drawable.icon_logout);

        menuList.add(model3);

        adapter = new NavigationAdapter( menuList, MainActivity.this);
        recyclerViewMenu.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerViewMenu.setLayoutManager(layoutManager);
        recyclerViewMenu.setAdapter(adapter);

    }


    @Override
    public void onMenuClick(NavigationModel data, int pos) {

        displaySelectedScreen(pos);

    }
}
