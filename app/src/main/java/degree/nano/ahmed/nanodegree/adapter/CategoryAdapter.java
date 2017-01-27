package degree.nano.ahmed.nanodegree.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import degree.nano.ahmed.nanodegree.Controller.OnRecycleClick;
import degree.nano.ahmed.nanodegree.R;
import degree.nano.ahmed.nanodegree.model.CategoryModel;


/**
 * Created by ahmed on 11/01/17.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    ArrayList<CategoryModel> arrayList;
     Activity activity;
    OnRecycleClick onRecycleClick;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCategoryName;
        ImageView imgCategory;
        LinearLayout linCat;
        public MyViewHolder(View view) {
            super(view);
             tvCategoryName = (TextView) view.findViewById(R.id.tvCategoryName);
             imgCategory = (ImageView) view.findViewById(R.id.imgCategory);
            linCat = (LinearLayout) view.findViewById(R.id.linCat);
         }
    }




    public CategoryAdapter(Activity activity, ArrayList<CategoryModel> arrayList, OnRecycleClick onRecycleClick) {
        this.arrayList = arrayList;
         this.activity =activity;
        this.onRecycleClick =onRecycleClick;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        CategoryModel categoryModel = arrayList.get(position);
       String text = categoryModel.getName();
       holder.tvCategoryName.setText(text);
        Picasso.with(activity)
                .load(categoryModel.getUrlPath())
                .into(holder.imgCategory);

        holder.linCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecycleClick.onClickRecycle(position);
            }
        });





    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
