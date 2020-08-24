package com.example.timify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.Holder> {

    ArrayList<alarmer> arrayList=new ArrayList<>();
    Context context;

    public AlarmAdapter(ArrayList<alarmer> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_element,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.time.setText(arrayList.get(position).getTime());
        String days_setup="";
        Boolean[] list=arrayList.get(position).getDayList();
        String[] days=new String[]{"Mon,",
        "Tue,",
        "Wed,",
        "Thu,",
        "Fri,",
        "Sat,",
        "Sun"};

        for(int i=0;i<7;i++){
            if(list[i]){
                days_setup=days_setup.concat(days[i]);
            }
        }

        if(days_setup.equals("")){
            days_setup="Single";
        }

        holder.days.setText(days_setup);
        holder.days.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        TextView time,days;

        public Holder(@NonNull View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.time);
            days=itemView.findViewById(R.id.days);
            days.setSingleLine(true);
            days.setSelected(true);
        }
    }

    public  void note(){
        notifyItemRemoved(0);
    }
}
