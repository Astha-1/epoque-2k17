package edu.kiet.www.epoque2017.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import edu.kiet.www.epoque2017.CardObjects.RegisteredEventCard;
import edu.kiet.www.epoque2017.Models.ProfileDataumPOJO;
import edu.kiet.www.epoque2017.R;

/**
 * Created by Shrey on 02-02-2017.
 */

public class RegisteredEventsAdapter extends RecyclerView.Adapter<RegisteredEventsAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    Context context;
   ProfileDataumPOJO data;

    public RegisteredEventsAdapter(Context context, ProfileDataumPOJO data){
        inflater=LayoutInflater.from(context);
        this.data=data;
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.registered_events,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.EventName.setText(data.getEventName().get(position));

    }

    @Override
    public int getItemCount() {
        return data.getEventName().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView EventName;



        public MyViewHolder(View itemView) {
            super(itemView);
            EventName=(TextView) itemView.findViewById(R.id.event_name);
        }
    }
}