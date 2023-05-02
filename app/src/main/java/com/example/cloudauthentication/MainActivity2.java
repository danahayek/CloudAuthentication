package com.example.cloudauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cloudauthentication.model.profile_data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity2 extends AppCompatActivity {

    EditText email_txt2 , pass_txt2 , name_txt2 , phone_txt2;
    Button log_btn;
    ProgressBar progressBar2;
    FirebaseAuth firebaseAuth ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        firebaseAuth = FirebaseAuth.getInstance();

        name_txt2=findViewById(R.id.name_log);
        phone_txt2=findViewById(R.id.phone_log);
        email_txt2=findViewById(R.id.email_log);
        pass_txt2=findViewById(R.id.password_log);
        log_btn=findViewById(R.id.button_log);
        progressBar2=findViewById(R.id.progressBar2);


        //notification
        FirebaseMessaging.getInstance().subscribeToTopic("profile")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.e("d","done");
                        }else {
                            Log.e("d","failed");
                        }
                    }
                });

        log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                get_user();
            }
        });
    }
    public void get_user(){
        progressBar2.setVisibility(View.VISIBLE);
        String name = name_txt2.getText().toString();
        String email = email_txt2.getText().toString();
        String pass = pass_txt2.getText().toString();
        String phone = phone_txt2.getText().toString();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(MainActivity2.this,"please fill your phone",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(MainActivity2.this,"please fill your name",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(MainActivity2.this,"please fill your email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(MainActivity2.this,"please fill your password",Toast.LENGTH_LONG).show();
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email,pass)

                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity2.this,"welcome",Toast.LENGTH_LONG).show();
                            progressBar2.setVisibility(View.GONE);
                            Intent intent=new Intent(MainActivity2.this,Profile.class);
                            startActivity(intent);

                            db.collection("profile").document(firebaseAuth.getUid())
                                    .set(new profile_data(name,email,pass,phone));

                        }else{
                            Toast.makeText(MainActivity2.this,"log failed",Toast.LENGTH_LONG).show();
                            progressBar2.setVisibility(View.GONE);
                        }
                    }
                });

    }
}