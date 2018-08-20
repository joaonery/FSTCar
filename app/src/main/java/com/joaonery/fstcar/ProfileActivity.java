package com.joaonery.fstcar;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joaonery.fstcar.model.User;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private MyApplication myApp;
    private DatabaseReference databaseRef;
    private String uid;

    private Toolbar toolbar;
    private ProgressBar progress;
    private CircleImageView civAvatar;
    private TextInputLayout tilFirstName;
    private EditText etFirstName;
    private TextInputLayout tilLastName;
    private EditText etLastName;
    private TextInputLayout tilEmail;
    private EditText etEmail;
    private TextInputLayout tilGender;
    private EditText etGender;
    private Button btUpdate;

    private static final int MENU_EDIT_PROFILE= 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialize();

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = new User();

                u.setFirstName(dataSnapshot.child(uid).child("firstName").getValue(String.class));
                u.setLastName(dataSnapshot.child(uid).child("lastName").getValue(String.class));
                u.setEmail(dataSnapshot.child(uid).child("email").getValue(String.class));
                u.setGender(dataSnapshot.child(uid).child("gender").getValue(String.class));

                Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/fstcar-541e8.appspot.com/o/" + uid + "%2Favatar.jpg?alt=media&token=51e1e7ff-b1a7-45a0-8695-7c8bda305719").into(civAvatar);
                etFirstName.setText(u.getFirstName());
                etLastName.setText(u.getLastName());
                etEmail.setText(u.getEmail());
                etGender.setText(u.getGender());

                tilFirstName.setEnabled(false);
                tilLastName.setEnabled(false);
                tilEmail.setEnabled(false);
                tilGender.setEnabled(false);

                civAvatar.setVisibility(View.VISIBLE);
                tilFirstName.setVisibility(View.VISIBLE);
                tilLastName.setVisibility(View.VISIBLE);
                tilEmail.setVisibility(View.VISIBLE);
                tilGender.setVisibility(View.VISIBLE);

                progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);

        menu.add(MENU_EDIT_PROFILE, MENU_EDIT_PROFILE, MENU_EDIT_PROFILE, "Edit Profile")
                .setIcon(getResources().getDrawable(R.drawable.ic_create))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case MENU_EDIT_PROFILE:
                btUpdate.setVisibility(View.VISIBLE);
                tilFirstName.setEnabled(true);
                tilLastName.setEnabled(true);
                tilGender.setEnabled(true);

                btUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etFirstName.getText().toString().isEmpty() || etLastName.getText().toString().isEmpty() || etGender.getText().toString().isEmpty()){
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.g_toast_empty_fields), Toast.LENGTH_SHORT).show();
                        }else{
                            databaseRef.child(uid).child("firstName").setValue(etFirstName.getText().toString());
                            databaseRef.child(uid).child("lastName").setValue(etLastName.getText().toString());
                            databaseRef.child(uid).child("gender").setValue(etGender.getText().toString());
                            btUpdate.setVisibility(View.INVISIBLE);
                            tilFirstName.setEnabled(false);
                            tilLastName.setEnabled(false);
                            tilGender.setEnabled(false);
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialize(){
        //Firebase
        myApp = new MyApplication();
        myApp.setmAuth(FirebaseAuth.getInstance());
        myApp.setFirebaseUser(myApp.getmAuth().getCurrentUser());
        databaseRef = FirebaseDatabase.getInstance().getReference("users");
        uid = myApp.getFirebaseUser().getUid();

        //References
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        progress = findViewById(R.id.pa_progress);
        civAvatar = findViewById(R.id.about_civ_avatar);
        tilFirstName = findViewById(R.id.pa_til_first_name);
        etFirstName = findViewById(R.id.pa_et_first_name);
        tilLastName = findViewById(R.id.pa_til_last_name);
        etLastName = findViewById(R.id.pa_et_last_name);
        tilEmail = findViewById(R.id.pa_til_email);
        etEmail = findViewById(R.id.pa_et_email);
        tilGender = findViewById(R.id.pa_til_gender);
        etGender = findViewById(R.id.pa_et_gender);
        btUpdate = findViewById(R.id.pa_bt_update);

        //Components invisible at startup
        civAvatar.setVisibility(View.INVISIBLE);
        tilFirstName.setVisibility(View.INVISIBLE);
        tilLastName.setVisibility(View.INVISIBLE);
        tilEmail.setVisibility(View.INVISIBLE);
        tilGender.setVisibility(View.INVISIBLE);
        btUpdate.setVisibility(View.INVISIBLE);
    }
}