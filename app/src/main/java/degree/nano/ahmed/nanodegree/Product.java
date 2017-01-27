package degree.nano.ahmed.nanodegree;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.intrusoft.squint.DiagonalView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import degree.nano.ahmed.nanodegree.Controller.Utility;
import degree.nano.ahmed.nanodegree.model.ProductModel;

/**
 * Created by ahmed on 16/01/17.
 */
public class Product extends Fragment {

    TextView tvProductName,tvProductDescription,tvProductPrice,tvEmail,tvMobile;
    String categoryId,childNode,nodeName;
    LinearLayout linPerson;
    DiagonalView diagonal;
    public static final int MY_PERMISSIONS_CALL = 8;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product,container,false);
        declare(view);

        Bundle bundle = getArguments();
        categoryId = bundle.getString("categoryId");
        childNode = bundle.getString("childNode");
        nodeName =  bundle.getString("data");
        retrieveData();

        return view;
    }

    private void declare(View view){
        tvProductName = (TextView) view.findViewById(R.id.tvProductName);
        tvProductDescription = (TextView) view.findViewById(R.id.tvProductDescription);
        tvProductPrice = (TextView) view.findViewById(R.id.tvProductPrice);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvMobile = (TextView) view.findViewById(R.id.tvMobile);
        diagonal = (DiagonalView) view.findViewById(R.id.diagonal);
        linPerson = (LinearLayout) view.findViewById(R.id.linPerson);
        tvMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCallPermission();
            }
        });
        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.email(getActivity(),tvEmail.getText().toString());
            }
        });
    }

    private void retrieveData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference posts = database.getReference("category").child(categoryId).child(childNode).child(nodeName);

        posts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ProductModel results = dataSnapshot.getValue(new GenericTypeIndicator<ProductModel>() {});

                tvProductName.setText(results.getProductName());
                tvProductDescription.setText(results.getProductDescription());
                tvProductPrice.setText(results.getProductUrl());
                tvProductName.setText(results.getProductName());
                Picasso.with(getActivity()).load(results.getPrice()).into(diagonal);
            try{
                tvEmail.setText(results.getEmail());
                tvMobile.setText(results.getMobile());
            }catch (Exception e){
                linPerson.setVisibility(View.GONE);
         }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkCallPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CALL_PHONE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Call Permission Needed")
                        .setMessage("This app needs the Call permission, please accept to use call functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        MY_PERMISSIONS_CALL );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_CALL );
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_CALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {

                        String number = tvMobile.getText().toString();
                        Uri call = Uri.parse("tel:" + number);
                        Intent surf = new Intent(Intent.ACTION_DIAL, call);
                        startActivity(surf);

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
