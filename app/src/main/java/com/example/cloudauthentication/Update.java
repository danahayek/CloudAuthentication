package com.example.cloudauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cloudauthentication.model.profile_data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Update extends AppCompatActivity {
    EditText nameE,emailE,passwordE,phoneE;
    Button update;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth firebaseAuth;
    private String currentid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        firebaseAuth=FirebaseAuth.getInstance();

        nameE=findViewById(R.id.edit_name);
        emailE=findViewById(R.id.edit_email);
        passwordE=findViewById(R.id.edit_add);
        phoneE=findViewById(R.id.edit_phone);
        update=findViewById(R.id.update_btn);

        String newName = nameE.getText().toString();
        String newEmail = emailE.getText().toString();
        String newAdd = passwordE.getText().toString();
        String newPhone = phoneE.getText().toString();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_profile(newName,newEmail,newAdd,newPhone);

            }
        });


    }
     public void update_profile(final String name,String email, String address, String phone){
         currentid=firebaseAuth.getCurrentUser().getUid();

         db.collection("profile").document(currentid)
                 .update("name",nameE.getText().toString(),
                         "email",emailE.getText().toString(),
                         "password",passwordE.getText().toString(),
                         "phone",phoneE.getText().toString())

                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         Toast.makeText(Update.this,"update successfully",Toast.LENGTH_LONG).show();

//                         db.collection("profile").document(firebaseAuth.getUid())
//                                 .set(new profile_data(name,email,address,phone));

                         Intent intent=new Intent(Update.this,Profile.class);
                         startActivity(intent);


                     }
                 })
                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         Log.d("edit", "DocumentSnapshot successfully updated!");
                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Log.w("edit", "Error updating document", e);
                     }
                 });

     }
}