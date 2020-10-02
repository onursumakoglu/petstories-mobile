package com.example.petstories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getDataFromFirestore();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.insta_options_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_post) {
            Intent intentToUpload = new Intent(FeedActivity.this, UploadActivity.class);
            startActivity(intentToUpload);
        } else if (item.getItemId() == R.id.logout) {

            firebaseAuth.signOut();

            Intent intentToSignUp = new Intent(FeedActivity.this, SignInActivity.class);
            startActivity(intentToSignUp);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void getDataFromFirestore(){

        CollectionReference collectionReference = firebaseFirestore.collection("Post");
        collectionReference.orderBy("date").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){
                    Toast.makeText(FeedActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (value != null){
                    for (DocumentSnapshot snapshot : value.getDocuments()){
                        Map<String, Object> data = snapshot.getData();

                        String comment = (String) data.get("comment");
                        String email = (String) data.get("email");
                        String downloadUrl = (String) data.get("downloadurl");

                        System.out.println(comment);
                    }
                }
            }
        });


    }


}