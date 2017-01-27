package degree.nano.ahmed.nanodegree;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import degree.nano.ahmed.nanodegree.Controller.OnRecycleClick;
import degree.nano.ahmed.nanodegree.adapter.CategoryAdapter;
import degree.nano.ahmed.nanodegree.model.CategoryModel;
import degree.nano.ahmed.nanodegree.model.ProductModel;

/**
 * Created by ahmed on 16/01/17.
 */
public class Categories extends Fragment implements OnRecycleClick {

    RecyclerView reCategories;
    ArrayList<CategoryModel>categoryModels;
    OnRecycleClick onRecycleClick;
    String categoryId,childNode;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categories,container,false);
        declare(view);
        retrieveData();

        return view;
    }

    private void declare(View view){
        onRecycleClick = this;
        categoryModels = new ArrayList<>();
       reCategories = (RecyclerView) view.findViewById(R.id.reCategories);
       reCategories.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void retrieveData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference posts = database.getReference("category");

        posts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("rrvvv", dataSnapshot.getChildren().iterator().toString());
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    String name = (String) messageSnapshot.child("name").getValue();
                    String ids = (String) messageSnapshot.child("id").getValue();
                    String url = (String) messageSnapshot.child("urlPath").getValue();
                    CategoryModel categoryModel = new CategoryModel(name,ids,url);
                    categoryModels.add(categoryModel);
                    Log.d("rrvvv2", name);
                }


//categoryModels = dataSnapshot.getValue(new GenericTypeIndicator<ArrayList<CategoryModel>>() {
//});
//                HashMap<String, CategoryModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, CategoryModel>>() {});
//
//                List<CategoryModel> posts = new ArrayList<>(results.values());

//        Log.d("rrvvv4",""+posts.size());

//                for (CategoryModel post : posts) {
//                }
                reCategories.setAdapter(new CategoryAdapter(getActivity(),categoryModels,onRecycleClick));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClickRecycle(int pos) {

        Intent intent = new Intent(getActivity(),MapsActivity.class);
         intent.putExtra("categoryId",pos+"");
        intent.putExtra("childNode",categoryModels.get(pos).getId());
        Log.d("nnnn", pos+"  "+categoryModels.get(pos).getId());

        startActivity(intent);
    }
}
