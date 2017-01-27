package degree.nano.ahmed.nanodegree;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import degree.nano.ahmed.nanodegree.model.CategoryModel;
import degree.nano.ahmed.nanodegree.model.ProductModel;

public class MainActivity extends AppCompatActivity {

    LinearLayout linAdd,linCategory;
    int chosen = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        declare();
        action();
        if(savedInstanceState==null) {

            getSupportFragmentManager().beginTransaction().add(R.id.main, new Categories()).addToBackStack("").commit();
            chosen = 0;
            if(getIntent().getExtras()!=null){
                Fragment product = new Product();
                Bundle bundle = new Bundle();
                bundle.putString("data",getIntent().getExtras().getString("data"));
                bundle.putString("categoryId",getIntent().getExtras().getString("categoryId"));
                bundle.putString("childNode",getIntent().getExtras().getString("childNode"));
                product.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.main,product).addToBackStack("").commit();
            }

        }else{
            chosen=savedInstanceState.getInt("position",0);
        }



   }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt("position",chosen);

        super.onSaveInstanceState(outState, outPersistentState);
    }
    private void declare(){
        linAdd = (LinearLayout) findViewById(R.id.linAdd);
        linCategory = (LinearLayout) findViewById(R.id.linCategory);
    }

    private void action(){
        linAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosen = 0;
                getSupportFragmentManager().beginTransaction().add(R.id.main,new AddProducts()).addToBackStack("").commit();
            }
        });

        linCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosen = 1;
                getSupportFragmentManager().beginTransaction().add(R.id.main,new Categories()).addToBackStack("").commit();

            }
        });
    }
}
