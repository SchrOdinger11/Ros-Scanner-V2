package com.example.opticalcharacterrecognizer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class BugReport extends AppCompatActivity {
    EditText email;
    EditText query;
    Button btn;
    //FirebaseDatabase database = FirebaseDatabase.getInstance();
   // DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_report);

      //  myRef= database.getReference().child("Query");
        email=findViewById(R.id.editTextTextEmailAddress);
        query=findViewById(R.id.editTextTextMultiLine);
        btn=findViewById(R.id.button3);
    }

    public void sendData(View view)
    {
        String Expn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";


        HashMap<String,Object> hm=new HashMap<>();


        String datafieldQuery= query.getText().toString();
        String datafieldEmail=email.getText().toString();

        if(!datafieldEmail.isEmpty() && !datafieldEmail.isEmpty()) {

            if (email.getText().toString().trim().matches(Expn)) {
                hm.put("Email", datafieldEmail);
                hm.put("Query", datafieldQuery);
                FirebaseDatabase.getInstance().getReference().child("Entries").push()
                        .setValue(hm)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   @Override
                                                   public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                       Log.i("i","mwssage sent");


                                                   }
                                               }
                        ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.i("Illuminati","message not sent");
                    }
                });
            } else {
                email.setText("");
                Toast.makeText(getApplicationContext(), "Invalid email address,Enter again", Toast.LENGTH_SHORT).show();
            }
        }
        else if(datafieldEmail.isEmpty() || datafieldQuery.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Field Cannot be empty", Toast.LENGTH_SHORT).show();
        }














































//        String id=myRef.push().getKey();
//        if(!(TextUtils.isEmpty(datafieldQuery) && TextUtils.isEmpty(datafieldEmail))){
//            if(email.getText().toString().trim().matches(Expn)) {
//                data obj = new data(id, datafieldEmail, datafieldQuery);
//                myRef.child(id).setValue(obj);  //x
//                myRef.child(id).setValue(datafieldQuery); //x
//                myRef.child(id).setValue(datafieldEmail + " Query typed- " + datafieldQuery);
//                myRef.push().setValue(obj);
//
//                Toast.makeText(this, "Your Query is Sent", Toast.LENGTH_LONG).show();
//                Intent i=new Intent(getApplicationContext(),AnimatedMainActivity.class);
//                startActivity(i);
//            }
//            else{
//                Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//        else{
//            Toast.makeText(this,"The fields cannot be empty",Toast.LENGTH_LONG).show();
//        }


    }
}