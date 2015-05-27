package com.maubocanegra.earthquakemonitor.earthquakemonitor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mauro on 24/05/2015.
 */
public class Eq_RecyclerViewAdapter extends RecyclerView.Adapter<Eq_RecyclerViewAdapter.ViewHolder>{

    private ArrayList<EarthquakeObject> mDataset;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public View mView;
        public ViewHolder(View v){
            super(v);
            mView=v;
        }
    }

    OnClickedItemListener onClickedItemListener;
    public interface OnClickedItemListener{
        public void onItemClicked(Object[] obj);
    }

    public Eq_RecyclerViewAdapter(ArrayList<EarthquakeObject> myDataset, OnClickedItemListener listener, Context c){
        onClickedItemListener = listener;
        mDataset = myDataset;
        context=c;
    }

    @Override
    public Eq_RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.earthquake_list_layout,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final Eq_RecyclerViewAdapter.ViewHolder holder, final int position) {
        EarthquakeObject eqObj = mDataset.get(position);
        ((TextView) holder.mView.findViewById(R.id.title)).setText(eqObj.getTitle());
        ((TextView) holder.mView.findViewById(R.id.place)).setText(eqObj.getDate());

        int[] colors = {
                context.getResources().getColor(R.color.material_green_600),context.getResources().getColor(R.color.material_green_800),context.getResources().getColor(R.color.material_green_900),
                context.getResources().getColor(R.color.material_amber_400),context.getResources().getColor(R.color.material_amber_600),context.getResources().getColor(R.color.material_amber_800),
                context.getResources().getColor(R.color.material_deep_orange_400),context.getResources().getColor(R.color.material_red_500),context.getResources().getColor(R.color.material_red_900)};
        holder.mView.findViewById(R.id.warningColor).setBackgroundColor(colors[(int)eqObj.getMag()]);

        ((TextView) holder.mView.findViewById(R.id.mag)).setText(context.getResources().getString(R.string.magni_text) + eqObj.getMag());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedItemListener.onItemClicked(new Object[]{position, mDataset.get(position), holder.mView});
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
