package edu.northeastern.numad24su_plateperfect;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class aChildAdapterClass extends RecyclerView.Adapter<aChildAdapterClass.ViewHolder> {

    List<rvChildModelClass> rvChildModelClassList;
    Context context;

    public aChildAdapterClass(List<rvChildModelClass> rvChildModelClassList, Context context) {
        this.rvChildModelClassList = rvChildModelClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public aChildAdapterClass.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_child_layout, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull aChildAdapterClass.ViewHolder holder, int position) {
        holder.ivChildImage.setImageResource(rvChildModelClassList.get(position).image);
        //holder.ivChildImage.setOnClickListener(); //ToDO direct to recipe page

    }

    @Override
    public int getItemCount() {
        return rvChildModelClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivChildImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivChildImage=itemView.findViewById(R.id.iv_child_item);
        }
    }
}
