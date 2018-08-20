package com.joaonery.fstcar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.joaonery.fstcar.model.User;

public class MainActivity extends AppCompatActivity {

    //Firebase
    private MyApplication myApp;

    private ImageView ivLogo;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private EditText etEmail;
    private EditText etPassword;
    private Button btLogin;
    private TextView tvCreateAccount;
    private TextView tvForgotPassword;
    private ProgressBar progress;

    private final int LOADING_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress.setVisibility(View.INVISIBLE);
                tilEmail.setVisibility(View.VISIBLE);
                tilPassword.setVisibility(View.VISIBLE);
                btLogin.setVisibility(View.VISIBLE);
                tvCreateAccount.setVisibility(View.VISIBLE);
                tvForgotPassword.setVisibility(View.VISIBLE);
            }
        }, LOADING_TIME);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etEmail.getText().toString().isEmpty() && etPassword.getText().toString().isEmpty()){
                    toast(getResources().getString(R.string.g_toast_empty_fields));
                }else{
                    progress.setVisibility(View.VISIBLE);

                    myApp.getmAuth().signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(!task.isSuccessful()){
                                        toast(getResources().getString(R.string.ma_toast_login_error));
                                    }else{
                                        Intent it = new Intent(MainActivity.this, HomeActivity.class);
                                        startActivity(it);
                                        finish();
                                    }
                                    progress.setVisibility(View.INVISIBLE);
                                }
                            });
                }
            }
        });

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(it);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etEmail.getText().toString().isEmpty()){
                    progress.setVisibility(View.VISIBLE);

                    User u = new User();

                    u.setEmail(etEmail.getText().toString());

                    myApp.getmAuth().sendPasswordResetEmail(u.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                toast(getResources().getString(R.string.ma_toast_forgot_password_success));
                            }
                            progress.setVisibility(View.GONE);
                        }
                    });
                }else{
                    toast(getResources().getString(R.string.ma_toast_forgot_password_failed));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        myApp.getmAuth().addAuthStateListener(myApp.getmAuthStateListener());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(myApp.getmAuthStateListener() != null){
            myApp.getmAuth().removeAuthStateListener(myApp.getmAuthStateListener());
        }
    }

    public void initialize(){
        //Firebase
        myApp = new MyApplication();
        myApp.setmAuth(FirebaseAuth.getInstance());
        myApp.setFirebaseUser(myApp.getmAuth().getCurrentUser());
        myApp.setmAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(myApp.getFirebaseUser() != null){
                    Intent it = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(it);
                    finish();
                }
            }
        });

        //References
        ivLogo = findViewById(R.id.ma_iv_logo);
        tilEmail = findViewById(R.id.ma_til_email);
        tilPassword = findViewById(R.id.ma_til_password);
        etEmail = findViewById(R.id.ma_et_email);
        etPassword = findViewById(R.id.ma_et_password);
        btLogin = findViewById(R.id.ma_bt_sign_in);
        tvCreateAccount = findViewById(R.id.ma_tv_sign_up);
        tvForgotPassword = findViewById(R.id.ma_tv_forgot_password);
        progress = findViewById(R.id.ma_progress);

        //Components invisible at startup
        tilEmail.setVisibility(View.INVISIBLE);
        tilPassword.setVisibility(View.INVISIBLE);
        btLogin.setVisibility(View.INVISIBLE);
        tvCreateAccount.setVisibility(View.INVISIBLE);
        tvForgotPassword.setVisibility(View.INVISIBLE);
    }

    private void toast(String msg){
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }
}