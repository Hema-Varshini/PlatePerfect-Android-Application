package edu.northeastern.numad24su_plateperfect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.squareup.picasso.Picasso;


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
        rvChildModelClass model = rvChildModelClassList.get(position);
        String url = model.getLink();
        Picasso.get().load(url).into(holder.ivChildImage);
        holder.ivChildTitle.setText(rvChildModelClassList.get(position).recipe);
        //holder.ivChildImage.setOnClickListener(); //ToDO direct to recipe page

    }

    @Override
    public int getItemCount() {
        return rvChildModelClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivChildImage;
        TextView ivChildTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivChildImage=itemView.findViewById(R.id.iv_child_item);
            ivChildTitle=itemView.findViewById(R.id.iv_recipe_Title);
        }
    }
}
