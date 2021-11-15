package com.example.cashbooster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataDisplayActivity extends AppCompatActivity {

    ListView allDataListView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String collectionName = "GamePortalsRecords";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);

        allDataListView = findViewById(R.id.allDataListView);

        db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult()){

                        String AmountRec = String.valueOf(documentSnapshot.get("Amount"));
                        String GameCodeRec = String.valueOf(documentSnapshot.get("GameCode"));
                        String GameStateRec = String.valueOf(documentSnapshot.get("GameState"));
                        String userID = String.valueOf(documentSnapshot.get("UserID"));
                        String GameTypeRec = String.valueOf(documentSnapshot.get("GameType"));
                        String Time = String.valueOf(documentSnapshot.get("TimeRecorded"));


                        FirestoreBucketObj fireStoreBucket = new FirestoreBucketObj(AmountRec,GameCodeRec,GameStateRec,Time,GameTypeRec,userID);

                        //dynamic adding to fireStore bucket object
                        ArrayList<FirestoreBucketObj> bucketObjArrayList = new ArrayList<>();
                        bucketObjArrayList.add(fireStoreBucket);

                        //adding elements to a LIstAdapter
                        FireStoreBucketAdapter adapter = new FireStoreBucketAdapter(this,R.layout.adapter_view_layout,bucketObjArrayList);
                        allDataListView.setAdapter(adapter);
                    }
                }else{
                    Toast.makeText(getApplicationContext(),task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}