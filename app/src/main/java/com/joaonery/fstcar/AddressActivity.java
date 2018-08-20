package com.joaonery.fstcar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joaonery.fstcar.adapter.AddressAdapter;
import com.joaonery.fstcar.model.Address;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import java.util.ArrayList;

public class AddressActivity extends AppCompatActivity {

    private MyApplication myApp;
    private DatabaseReference databaseRef;
    private String uid;

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ProgressBar progress;
    private RecyclerView rvAdresses;
    private ArrayList<Address> adresses;
    private AddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        initialize();

        databaseRef.child(uid).child("address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adresses.clear();

                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Address a = data.getValue(Address.class);
                    a.setKey(data.getKey());
                    adresses.add(a);
                }

                progress.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter.setOnItemClickListener(new AddressAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                //Nothing here
            }

            @Override
            public void onItemLongClick(final int position, View v) {
                AlertDialog.Builder msg = new AlertDialog.Builder(AddressActivity.this);
                msg.setTitle(getResources().getString(R.string.g_alert_title));
                msg.setMessage(getResources().getString(R.string.aa_alert_msg));
                msg.setNegativeButton(getResources().getString(R.string.g_alert_negative_button), null);
                msg.setPositiveButton(getResources().getString(R.string.g_alert_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Address a = adresses.get(position);
                        databaseRef.child(uid).child("address").child(a.getKey()).removeValue();

                        toast(getResources().getString(R.string.aa_toast_address_removed));
                    }
                });
                msg.show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adresses.size() < 2){
                    Intent it = new Intent(AddressActivity.this, RegisterAddressActivity.class);
                    startActivity(it);
                }else{
                    toast(getResources().getString(R.string.aa_toast_address_limit));
                }

            }
        });
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
        fab = findViewById(R.id.fab);
        progress = findViewById(R.id.ca_progress);
        rvAdresses = findViewById(R.id.ca_rv_adresses);

        adresses = new ArrayList<>();
        adapter = new AddressAdapter(AddressActivity.this, adresses);
        rvAdresses.setAdapter(adapter);
        rvAdresses.setHasFixedSize(true);
        rvAdresses.setLayoutManager(new LinearLayoutManager(this));
    }

    private void toast(String msg){
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }
}