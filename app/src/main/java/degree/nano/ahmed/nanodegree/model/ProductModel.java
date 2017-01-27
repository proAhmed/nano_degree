package degree.nano.ahmed.nanodegree.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ahmed on 17/01/17.
 */
public class ProductModel implements Parcelable{

    String productName;
    String productDescription;
    String productUrl;
    String price;
    double latitude;
    double longitude;
    String mobile;
    String email;

    public ProductModel() {
    }

    public ProductModel(String productName, String productDescription, String productUrl, String price) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productUrl = productUrl;
        this.price = price;
    }

    public ProductModel(String productName, String productDescription, String productUrl, String price, double longitude, double latitude) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productUrl = productUrl;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public ProductModel(String productName, String productDescription, String productUrl, String price,String email,
                        String mobile,double longitude, double latitude) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productUrl = productUrl;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mobile = mobile;
        this.email = email;

    }

    protected ProductModel(Parcel in) {
        productName = in.readString();
        productDescription = in.readString();
        productUrl = in.readString();
        price = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        mobile = in.readString();
        email = in.readString();
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productName);
        parcel.writeString(productDescription);
        parcel.writeString(productUrl);
        parcel.writeString(price);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(mobile);
        parcel.writeString(email);
    }
}
