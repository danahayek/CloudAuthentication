package com.example.cloudauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudauthentication.model.profile_data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText email_txt , pass_txt , name_txt , phone_txt;
    Button sign_btn;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth ;
    TextView go_toLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        email_txt=findViewById(R.id.email_sign);
        phone_txt=findViewById(R.id.phone_sign);
        pass_txt=findViewById(R.id.password_sign);
        sign_btn=findViewById(R.id.button_sign);
        progressBar=findViewById(R.id.progressBar_sign);
        name_txt=findViewById(R.id.name_sign);
        go_toLog=findViewById(R.id.go_log);



//// notification
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


        go_toLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);
            }
        });

        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add_user();
            }
        });
    }
    public void add_user(){
        progressBar.setVisibility(View.VISIBLE);

        String name = name_txt.getText().toString();
        String email = email_txt.getText().toString();
        String pass = pass_txt.getText().toString();
        String phone = phone_txt.getText().toString();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(MainActivity.this,"please fill your phone",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(MainActivity.this,"please fill your name",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(MainActivity.this,"please fill your email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(MainActivity.this,"please fill your password",Toast.LENGTH_LONG).show();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(email,pass)


                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"welcome",Toast.LENGTH_LONG).show();

                            progressBar.setVisibility(View.GONE);

                            db.collection("profile").document(firebaseAuth.getUid())
                                    .set(new profile_data(name,email,pass,phone));

                            Intent intent=new Intent(MainActivity.this,Profile.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(MainActivity.this,"sign up failed",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    ///////add to firbase
    public void addProfile (){
        String name = name_txt.getText().toString();
        String email = email_txt.getText().toString();
        String password = pass_txt.getText().toString();
        String phone = phone_txt.getText().toString();

        Map<String, Object> profile = new HashMap<>();
        if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !phone.isEmpty()) {

            profile.put("name",name);
            profile.put("email",email);
            profile.put("password",password);
            profile.put("phone",phone);



            db.collection("profile")
                    .add(profile)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.e("dana", "Note added successfully ");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("dana", "Failed to add profile", e);
                        }
                    });

        }else {
            Toast.makeText(this,"please fill field" ,Toast.LENGTH_SHORT).show();
        }
    }
}