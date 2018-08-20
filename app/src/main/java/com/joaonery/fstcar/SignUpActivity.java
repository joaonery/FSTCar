package com.joaonery.fstcar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.joaonery.fstcar.model.User;
import android.Manifest;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;


public class SignUpActivity extends AppCompatActivity {

    private MyApplication myApp;
    private DatabaseReference databaseRef;
    private StorageReference storageRef;
    private String uid;

    private CircleImageView civAvatar;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPassword;
    private RadioGroup rgGender;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private ProgressBar progress;
    private Button btSignUp;

    private final int CAMERA_REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialize();

        if (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.CAMERA},0);
            } else {
                ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.CAMERA},0);
            }
        }

        civAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this, Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.CAMERA},0);
                    } else {
                        ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.CAMERA},0);
                    }
                } else {
                    Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(it, CAMERA_REQUEST_CODE);
                }
            }
        });

        civAvatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(civAvatar.getDrawable().getConstantState() !=  getResources().getDrawable(R.drawable.ic_account_circle).getConstantState()){
                    AlertDialog.Builder msg = new AlertDialog.Builder(SignUpActivity.this);
                    msg.setTitle(getResources().getString(R.string.g_alert_title));
                    msg.setMessage(getResources().getString(R.string.su_alert_msg));
                    msg.setPositiveButton(getResources().getString(R.string.g_alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            civAvatar.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_circle));
                        }
                    });
                    msg.setNegativeButton(getResources().getString(R.string.g_alert_negative_button), null);
                    msg.show();
                }
                return true;
            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstName = etFirstName.getText().toString();
                final String lastName = etLastName.getText().toString();
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                final String gender;

                if(rgGender.getCheckedRadioButtonId() == rbMale.getId()){
                    gender = rbMale.getText().toString();
                }else if(rgGender.getCheckedRadioButtonId() == rbFemale.getId()){
                    gender = rbFemale.getText().toString();
                }else{
                    gender = "-";
                }

                if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()){
                    toast(getResources().getString(R.string.g_toast_empty_fields));
                }else{
                    progress.setVisibility(View.VISIBLE);
                    myApp.getmAuth().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()){
                                        toast(getResources().getString(R.string.su_toast_sign_up_error));
                                    }else{
                                        User u = new User(firstName, lastName, email, gender);

                                        myApp.setFirebaseUser(myApp.getmAuth().getCurrentUser());
                                        uid = myApp.getFirebaseUser().getUid();
                                        databaseRef = FirebaseDatabase.getInstance().getReference("users");
                                        storageRef = FirebaseStorage.getInstance().getReference(uid);

                                        civAvatar.setDrawingCacheEnabled(true);
                                        civAvatar.buildDrawingCache();

                                        Bitmap bitmap = ((BitmapDrawable) civAvatar.getDrawable()).getBitmap();
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                        byte[] data = baos.toByteArray();

                                        databaseRef.child(uid).setValue(u);
                                        storageRef.child("avatar.jpg").putBytes(data);

                                        Intent it = new Intent(SignUpActivity.this, HomeActivity.class);
                                        startActivity(it);
                                        finish();
                                    }
                                    progress.setVisibility(View.INVISIBLE);
                                }
                            });
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                Bitmap avatar = (Bitmap) data.getExtras().get("data");
                civAvatar.setImageBitmap(avatar);
            }
        }
    }

    private void initialize(){
        //Firebase
        myApp = new MyApplication();
        myApp.setmAuth(FirebaseAuth.getInstance());

        //References
        civAvatar = findViewById(R.id.about_civ_avatar);
        etFirstName = findViewById(R.id.su_et_first_name);
        etLastName = findViewById(R.id.su_et_last_name);
        etEmail = findViewById(R.id.su_et_email);
        etPassword = findViewById(R.id.su_et_password);
        rgGender = findViewById(R.id.su_rg_gender);
        rbMale = findViewById(R.id.su_rb_male);
        rbFemale = findViewById(R.id.su_rb_female);
        progress = findViewById(R.id.su_progress);
        btSignUp = findViewById(R.id.su_bt_sign_up);

        //Component invisible at startup
        progress.setVisibility(View.INVISIBLE);
    }

    private void toast(String msg){
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }
}