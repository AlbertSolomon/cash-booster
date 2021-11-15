package com.example.cashbooster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Records> {
    public ListAdapter(Context context, ArrayList<Records> productArrayList){
        super(context,R.layout.list_records,productArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Records records = getItem(position);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_records, parent, false);

            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_records,null);
           // convertView  = inflator.inflate(R.layout.list_records, parent, false);
        }

        TextView gameCode = convertView.findViewById(R.id.gameCode);
        TextView gameState = convertView.findViewById(R.id.gameState);
        TextView gameAmount = convertView.findViewById(R.id.gameAmount);
        TextView timeMessage = convertView.findViewById(R.id.timeMessage);
        TextView gameType = convertView.findViewById(R.id.gameType);

        //productName.setText(product.productName);
        gameCode.setText(records.GameCode);
        gameAmount.setText(records.Amount);
        gameState.setText(records.GameState);
        timeMessage.setText(records.currentTime);
        gameType.setText(records.GameType);

        //return super.getView(position, convertView, parent);
        return convertView;
    }
}
