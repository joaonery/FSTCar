package com.joaonery.fstcar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class HomeActivity extends AppCompatActivity {

    private MyApplication myApp;

    private Toolbar toolbar;
    private Drawer result = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialize();

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(HomeActivity.this)
                .withHeaderBackground(R.drawable.typography)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .build();

        result = new DrawerBuilder()
                .withActivity(HomeActivity.this)
                .withTranslucentStatusBar(false)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(getResources().getString(R.string.title_activity_home))
                                .withIcon(GoogleMaterial.Icon.gmd_home),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName(getResources().getString(R.string.title_activity_profile))
                                .withIcon(GoogleMaterial.Icon.gmd_person)
                                .withIdentifier(1),
                        new SecondaryDrawerItem()
                                .withName(getResources().getString(R.string.title_activity_address))
                                .withIdentifier(2)
                                .withIcon(GoogleMaterial.Icon.gmd_map),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName(getResources().getString(R.string.title_activity_about))
                                .withIdentifier(3)
                                .withIcon(GoogleMaterial.Icon.gmd_help),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName(getResources().getString(R.string.title_drawer_logout))
                                .withIdentifier(4)
                                .withIcon(GoogleMaterial.Icon.gmd_exit_to_app)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        Intent it;

                        switch ((int)drawerItem.getIdentifier()){
                            case 1:
                                it = new Intent(HomeActivity.this, ProfileActivity.class);
                                startActivity(it);
                                break;
                            case 2:
                                it = new Intent(HomeActivity.this, AddressActivity.class);
                                startActivity(it);
                                break;
                            case 3:
                                it = new Intent(HomeActivity.this, AboutActivity.class);
                                startActivity(it);
                                break;
                            case 4:
                                myApp.getmAuth().signOut();
                                it = new Intent(HomeActivity.this, MainActivity.class);
                                startActivity(it);
                                finish();
                                break;
                        }

                        return false;
                    }
                }).build();
    }

    private void initialize(){
        //Firebase
        myApp = new MyApplication();
        myApp.setmAuth(FirebaseAuth.getInstance());

        //References
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}