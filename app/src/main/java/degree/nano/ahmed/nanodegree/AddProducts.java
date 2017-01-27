package degree.nano.ahmed.nanodegree;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import degree.nano.ahmed.nanodegree.Controller.StoreData;
import degree.nano.ahmed.nanodegree.model.CategoryModel;
import degree.nano.ahmed.nanodegree.model.ProductModel;

/**
 * Created by ahmed on 16/01/17.
 */
public class AddProducts extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    EditText edProductName,edProductDesc,edPrice,edMobile,edEmail;
    Button btnAddProduct;
     ArrayList<CategoryModel>arrayList;
   // MaterialBetterSpinner textView ;
    String ids;
    int poss;
    private DatabaseReference mDatabase;
    ImageView imgAdd;
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageRef;
    String urlPath;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    LocationListener locationListener;
    String node1="0",node2="C1";
    private RadioGroup radio;

    private RadioButton radioCar, radioMobil, radioLaptop;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.add_product,container,false);
        declare(view);
        if(savedInstanceState!=null){
       //     retrieveData();
            try {
                edProductName.setText(savedInstanceState.getString("product_name"));
                edProductDesc.setText(savedInstanceState.getString("product_details"));
                edPrice.setText(savedInstanceState.getString("price"));
                edMobile.setText(savedInstanceState.getString("product_name"));
                edEmail.setText(savedInstanceState.getString("product_name"));
                node1 = savedInstanceState.getString("node1");
                node2 = savedInstanceState.getString("node2");
                Bitmap bitmap = savedInstanceState.getParcelable("uri");
                imgAdd.setImageBitmap(bitmap);
            }catch (Exception e){

            }
        }
        action();
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("product_name",edProductDesc.getText().toString() );
        outState.putString("product_details", edProductDesc.getText().toString());
        outState.putString("price", edPrice.getText().toString());
        outState.putString("mobile", edMobile.getText().toString());
        outState.putString("email", edEmail.getText().toString());
        outState.putParcelable("uri",filePath);
        outState.putString("node1", node1);
        outState.putString("node2", node2);

        super.onSaveInstanceState(outState);


    }
    private void declare(View view){
        radio = (RadioGroup) view.findViewById(R.id.radio);



        radioCar = (RadioButton) view.findViewById(R.id.radioCar);
        radioMobil = (RadioButton) view.findViewById(R.id.radioMobil);
        radioLaptop = (RadioButton) view.findViewById(R.id.radioLaptop);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
             public void onCheckedChanged(RadioGroup group, int checkedId) {
                 // find which radio button is selected
                 if(checkedId == R.id.radioCar) {
                     node1 = "0";
                     node2 = "C1";
                 } else if(checkedId == R.id.radioMobil) {
                     node1 = "1";
                     node2 = "M1";

                 } else {
                     node1 = "2";
                     node2 = "L1";

                 }
             }

         });

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://glaring-torch-4375.appspot.com");
        arrayList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("category");
        edProductName = (EditText) view.findViewById(R.id.edProductName);
        edProductDesc = (EditText) view.findViewById(R.id.edProductDesc);
        edPrice = (EditText) view.findViewById(R.id.edPrice);
        edMobile = (EditText) view.findViewById(R.id.edMobile);
        edEmail = (EditText) view.findViewById(R.id.edEmail);
        btnAddProduct = (Button) view.findViewById(R.id.btnAddProduct);
    //    textView = (MaterialBetterSpinner) view.findViewById(R.id.spCategories);
        imgAdd = (ImageView) view.findViewById(R.id.imgAdd);
    }

    private void action(){


        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edProductName.getText().toString().equals("")||edProductDesc.getText().toString().equals("")||
                edPrice.getText().toString().equals("")||edEmail.getText().toString().equals("")||edMobile.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Please insert all data",Toast.LENGTH_LONG).show();
                }else{
                    getLoc();
                    addData();
                }
            }
        });
    }



//    private void retrieveData(){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference posts = database.getReference("category");
//
//        posts.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
//
//                    String name = (String) messageSnapshot.child("name").getValue();
//                    String ids = (String) messageSnapshot.child("id").getValue();
//                    String url = (String) messageSnapshot.child("urlPath").getValue();
//                    CategoryModel categoryModel = new CategoryModel(name,ids,url);
//                    arrayList.add(categoryModel);
//                    Log.d("rrvvv", name);
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void getLoc(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
        }
    }

    private void addData(){
        ProductModel productModel = new ProductModel( edProductName.getText().toString(),
                edProductDesc.getText().toString(),urlPath,edPrice.getText().toString(),
                edEmail.getText().toString(),edMobile.getText().toString()
                ,new StoreData(getActivity()).getLong(),new StoreData(getActivity()).getLatitude());
        HashMap<String,ProductModel>productModelHashMap= new HashMap<>();
        productModelHashMap.put(edProductName.getText().toString(),productModel);

        mDatabase.child(node1).child(node2).child(edProductName.getText().toString()).setValue(productModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getActivity(),"data added successfully",Toast.LENGTH_LONG).show();

                                }
                            }
                        })
        ;
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
          filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imgAdd.setImageBitmap(bitmap);
                uploadFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = storageRef.child("images/");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            try{
                                urlPath = taskSnapshot.getDownloadUrl().toString();

                            }catch (Exception e){

                            }
                            //and displaying a success toast
                            Toast.makeText(getActivity(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                android. Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {


         new StoreData(getActivity()).setLatitude((float) location.getLatitude());
        new StoreData(getActivity()).setLong((float) location.getLongitude());
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
