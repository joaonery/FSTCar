package com.joaonery.fstcar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.joaonery.fstcar.model.Address;
import com.joaonery.fstcar.service.APIRetrofitService;
import com.joaonery.fstcar.service.CEPDeserializer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterAddressActivity extends AppCompatActivity {

    private MyApplication myApp;
    private DatabaseReference databaseRef;
    private String uid;

    private Gson g;
    private Retrofit retrofit;
    private APIRetrofitService service;

    private EditText etZipCode;
    private ImageView ivSearch;
    private EditText etState;
    private EditText etCity;
    private EditText etDistrict;
    private EditText etStreet;
    private EditText etNumber;
    private ProgressBar progress;
    private Button btRegister;
    private String child = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_address);

        initialize();

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etZipCode.getText().toString().isEmpty()){
                    toast(getResources().getString(R.string.ra_toast_fill_zip_code));
                }else{
                    progress.setVisibility(View.VISIBLE);

                    Call<Address> callAddress = service.getCEP(etZipCode.getText().toString());

                    callAddress.enqueue(new Callback<Address>() {
                        @Override
                        public void onResponse(Call<Address> call, Response<Address> response) {
                            if(response.isSuccessful()){
                                Address address = response.body();

                                etState.setText(address.getState());
                                etCity.setText(address.getCity());
                                etDistrict.setText(address.getDistrict());
                                etStreet.setText(address.getStreet());
                            }else{
                                toast(getResources().getString(R.string.ra_toast_invalid_zip_code));
                            }
                            progress.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFailure(Call<Address> call, Throwable t) {
                            toast(getResources().getString(R.string.g_toast_error) + t.getMessage());
                        }
                    });
                }
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etZipCode.getText().toString().isEmpty() || etState.getText().toString().isEmpty() || etCity.getText().toString().isEmpty() || etDistrict.getText().toString().isEmpty() ||etStreet.getText().toString().isEmpty() || etNumber.getText().toString().isEmpty()){
                    toast(getResources().getString(R.string.g_toast_empty_fields));
                }else {
                    progress.setVisibility(View.VISIBLE);

                    final Address address = new Address();

                    address.setZipCode(etZipCode.getText().toString());
                    address.setState(etState.getText().toString());
                    address.setCity(etCity.getText().toString());
                    address.setDistrict(etDistrict.getText().toString());
                    address.setStreet(etStreet.getText().toString());
                    address.setNumber(Integer.parseInt(etNumber.getText().toString()));

                    databaseRef.child(uid).child("address").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild("1")){
                                child = "2";
                            }

                            databaseRef.child(uid).child("address").child(child).setValue(address).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent it = new Intent();
                                    setResult(RESULT_OK, it);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    toast(getResources().getString(R.string.ra_toat_address_register_fail));
                                }
                            });
                            progress.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    private void initialize(){
        //Firebase
        myApp = new MyApplication();
        myApp.setmAuth(FirebaseAuth.getInstance());
        myApp.setFirebaseUser(myApp.getmAuth().getCurrentUser());
        uid = myApp.getFirebaseUser().getUid();
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        //Retrofit
        g = new GsonBuilder().registerTypeAdapter(Address.class, new CEPDeserializer()).create();
        retrofit = new Retrofit.Builder().baseUrl("https://viacep.com.br/ws/").addConverterFactory(GsonConverterFactory.create(g)).build();
        service = retrofit.create(APIRetrofitService.class);

        //References
        etZipCode = findViewById(R.id.ra_et_zip_code);
        ivSearch = findViewById(R.id.ra_iv_search);
        etState = findViewById(R.id.ra_et_state);
        etCity = findViewById(R.id.ra_et_city);
        etDistrict = findViewById(R.id.ra_et_district);
        etStreet = findViewById(R.id.ra_et_street);
        etNumber = findViewById(R.id.ra_et_number);
        progress = findViewById(R.id.ra_progress);
        btRegister = findViewById(R.id.ra_bt_register);

        //Component invisible at startup
        progress.setVisibility(View.INVISIBLE);
    }

    private void toast(String msg){
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }
}