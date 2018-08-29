package id.notation.kosanku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import id.notation.kosanku.fragments.IndekosFragment;
import id.notation.kosanku.fragments.KamarKosFragment;
import id.notation.kosanku.fragments.KaryawanFragment;
import id.notation.kosanku.fragments.UserDashboardFragment;
import id.notation.kosanku.utils.SharedPreferenceManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferenceManager sharedPreferenceManager;
    private TextView usernameNavTv;
    private TextView roleNavTv;
    private String role;
    NavigationView navigationView;
    FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        sharedPreferenceManager = new SharedPreferenceManager(getApplicationContext());
        role = sharedPreferenceManager.getAppUserRole();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_indekos);
        
        usernameNavTv = navigationView.getHeaderView(0).findViewById(R.id.username_nav_tv);
        roleNavTv = navigationView.getHeaderView(0).findViewById(R.id.role_nav_tv);

        usernameNavTv.setText(sharedPreferenceManager.getAppUsername());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Log.d("ROLE!", "onCreate: " + role);

        switch (role) {
            case "Admin":
                transaction.replace(R.id.main_container, new KaryawanFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case "Karyawan":
                transaction.replace(R.id.main_container, new KamarKosFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case "User":
                transaction.replace(R.id.main_container, new UserDashboardFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case "Pemilik":
                transaction.replace(R.id.main_container, new KamarKosFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;    
            default:
                transaction.replace(R.id.main_container, new UserDashboardFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }

        hideItem();
        Log.d("##", "onCreate: " + intent.getStringExtra("mainactivity.page"));

        if (intent.hasExtra("mainactivity.page")){
            redirectToFragment(intent.getStringExtra("mainactivity.page"));
        } else {
            if (role.equals("Admin"))
            redirectToFragment("karyawan");
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        transaction = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.id.nav_karyawan :
                transaction.replace(R.id.main_container, new KaryawanFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case R.id.nav_indekos:
                transaction.replace(R.id.main_container, new IndekosFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case R.id.nav_kamarkos:
                transaction.replace(R.id.main_container, new KamarKosFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case R.id.nav_logout :
                sharedPreferenceManager.clear();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void hideItem()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();

        if (role == "User") {
            nav_Menu.findItem(R.id.nav_karyawan).setVisible(false);
            nav_Menu.findItem(R.id.nav_indekos).setVisible(false);
            nav_Menu.findItem(R.id.nav_kamarkos).setVisible(false);
        }

        if (role == "Karyawan") {
            nav_Menu.findItem(R.id.nav_karyawan).setVisible(false);
            nav_Menu.findItem(R.id.nav_indekos).setVisible(false);
        }
    }

    public void redirectToFragment(String name) {
        transaction = getSupportFragmentManager().beginTransaction();
        Log.d("Redir: ", "redirectToFragment: " + name);
        Log.d("Redir: ", "redirectToFragment: " + name.equals("karyawan"));

        if (name.equals("karyawan")) {
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_karyawan));
        } else if (name.equals("indekos")) {
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_indekos));
        } else if (name.equals("kamarkos")) {
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_kamarkos));
        } else {
            if (sharedPreferenceManager.getAppUserRole() != "User") {
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_kamarkos));
            }
        }
    }
}
