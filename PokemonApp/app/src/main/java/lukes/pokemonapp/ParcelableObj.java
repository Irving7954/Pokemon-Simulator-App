package lukes.pokemonapp;

import android.os.Parcelable;
import android.os.Parcel;

public class ParcelableObj implements Parcelable {

    private String pokeName;

    public ParcelableObj(String name) {
        pokeName = name;
    }

    protected ParcelableObj(Parcel in) {
        pokeName = in.readString(); // Read fields in the SAME ORDER they are written
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(pokeName);
    }
    
    public static final Parcelable.Creator<ParcelableObj> CREATOR = new Creator<>() {
        public ParcelableObj createFromParcel(Parcel in) {
            return new ParcelableObj(in);
        }

        public ParcelableObj[] newArray(int size) {
            return new ParcelableObj[size];
        }
    };
}
