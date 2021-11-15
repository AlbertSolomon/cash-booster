package com.example.cashbooster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FireStoreBucketAdapter extends ArrayAdapter<FirestoreBucketObj> {
    private  Context myContext;
    public int mResource;

    public Context getMyContext() {
        return myContext;
    }

    public void setMyContext(Context myContext) {
        this.myContext = myContext;
    }



    public FireStoreBucketAdapter(@NonNull OnCompleteListener<QuerySnapshot> context, int resource, @NonNull List<FirestoreBucketObj> objects) {
        super((Context) context, resource, objects);
        myContext = (Context) context;
        mResource = resource;

    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String AmountRec = getItem(position).getAmount();
        String GameCodeRec = getItem(position).getGameCode();
        String GameStateRec = getItem(position).getGameState();
        String userID = getItem(position).getId();
        String GameTypeRec = getItem(position).getGameType();
        String Time = getItem(position).getCurrentTime();

        //FirestoreBucketObject with information
        FirestoreBucketObj bucketObj = new FirestoreBucketObj(AmountRec,GameCodeRec,GameStateRec,Time,GameTypeRec,userID);
        LayoutInflater inflater = LayoutInflater.from(myContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView gameCode = convertView.findViewById(R.id.gameCode);
        TextView gameState = convertView.findViewById(R.id.gameState);
        TextView gameAmount = convertView.findViewById(R.id.gameAmount);
        TextView timeMessage = convertView.findViewById(R.id.timeMessage);
        TextView gameType = convertView.findViewById(R.id.gameType);
        TextView fireStoreUserId = convertView.findViewById(R.id.fireStoreUserId);

        gameAmount.setText(AmountRec);
        gameCode.setText(GameCodeRec);
        gameState.setText(GameStateRec);
        fireStoreUserId.setText(userID);
        gameType .setText(GameTypeRec);
        timeMessage.setText(Time);

        return convertView;
    }

}
