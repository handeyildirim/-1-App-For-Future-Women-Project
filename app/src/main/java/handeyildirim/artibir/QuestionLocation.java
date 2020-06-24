package besteburhan.artibir;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by besteburhan on 15.9.2017.
 */

public class QuestionLocation {
    String latitude;
    String longitude;
    String meter;

    public  QuestionLocation(){}

    public QuestionLocation(String latitude, String longitude, String meter) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.meter = meter;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }
}

