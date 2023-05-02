package com.example.cloudauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {


    TextView name_pro ,email_pro,phoneNo_pro , password_pro;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ImageView image_profile;
    Button btn_edit;
    String uid;
    private FirebaseUser firebaseAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // PROFILE
        email_pro=findViewById(R.id.emailProfile);
        name_pro=findViewById(R.id.name);
        phoneNo_pro=findViewById(R.id.number);
        password_pro=findViewById(R.id.address);
        image_profile=findViewById(R.id.imageView);
        btn_edit=findViewById(R.id.go_to_update);


        firebaseAuth=FirebaseAuth.getInstance().getCurrentUser();
        uid= firebaseAuth.getUid().toString();



        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(Profile.this,Update.class);
                startActivity(intent);
            }
        });



        getProfile();


///notification////////////
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



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        DocumentReference docRef = db.collection("images").document("myimage");


        storageRef.child("profileWoman.png").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Display the image using Glide or any other image loading library
                        ImageView imageView = findViewById(R.id.imageView);
                        Glide.with(Profile.this).load(uri).into(imageView);
                    }
                });



    }
///////// get profile/////////////
public void getProfile (){
        db.collection("profile")
                .document(uid.toString())
                .get()

                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {

                                //String id = documentSnapshot.getId();
                                String name = documentSnapshot.getString("name");
                                String email = documentSnapshot.getString("email");
                                String password = documentSnapshot.getString("password");
                                String phone = documentSnapshot.getString("phone");


                                name_pro.setText(name);
                                email_pro.setText(email);
                                phoneNo_pro.setText(phone);
                                password_pro.setText(password);



                        }
                    }
                })






                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("get", "get failed ");


                    }
                });
    }

/////////edit profile///////////////


/////////option menu (edit)////////////
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.edit:
//                updateProfile(newName,newEmail,newAdd,newPhone);
//                Toast.makeText(this,"edit" ,Toast.LENGTH_SHORT).show();
//                return true;
//
//        }
//        return true;
//    }

}