package degree.nano.ahmed.nanodegree.model;

import java.util.ArrayList;

/**
 * Created by ahmed on 16/01/17.
 */
public class CategoryModel {

    String name;
    String id;
    String urlPath;
 //   ArrayList<ProductModel> productModels;

    public CategoryModel() {
    }


    public CategoryModel(String name, String id, String urlPath
       //     , ArrayList<ProductModel> productModels
    ) {
        this.name = name;
        this.id = id;
        this.urlPath = urlPath;
     //   this.productModels = productModels;
    }

//    public ArrayList<ProductModel> getProductModels() {
//        return productModels;
//    }
//
//    public void setProductModels(ArrayList<ProductModel> productModels) {
//        this.productModels = productModels;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "name='" + name ;
    }
}
