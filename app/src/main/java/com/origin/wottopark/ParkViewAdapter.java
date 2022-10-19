package com.origin.wottopark;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ParkViewAdapter extends RecyclerView.Adapter<ParkViewAdapter.MyViewHolder>{
    private ArrayList<ParkModel> mList = new ArrayList<>();

    public ParkViewAdapter(ArrayList<ParkModel> mList)
    {
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parkrow, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        holder.PlateNumber.setText(mList.get(position).getPlatenumber());
        holder.InputTime.setText(mList.get(position).getInputtime());
        holder.TicketNumber.setText(mList.get(position).getTicketnumber());
        holder.ParkedTime.setText(mList.get(position).getParkedtime());
        holder.TotalPrice.setText(mList.get(position).getTotalprice());
        holder.VehicleId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(v.getContext(),OutActivity.class);
                intent.putExtra("platenumber",mList.get(position).getPlatenumber());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView PlateNumber;
        private TextView ParkedTime;
        private TextView TicketNumber;
        private TextView InputTime;
        private TextView TotalPrice;
        private ImageButton VehicleId;

        public MyViewHolder(View view)
        {
            super(view);

            PlateNumber = view.findViewById(R.id.txtPlatenumber);
            ParkedTime = view.findViewById(R.id.txtParkedtime);
            TicketNumber = view.findViewById(R.id.txtTicketnumber);
            InputTime = view.findViewById(R.id.txtInputtime);
            TotalPrice = view.findViewById(R.id.txtTotalprice);
            VehicleId = view.findViewById(R.id.img_vehicleid);
        }
    }
}
