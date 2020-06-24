package besteburhan.artibir;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Maps extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    EditText editTextMeter ;
    Button buttonMeter;
    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    boolean mRequestingLocationUpdates = false;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker marker;
    Geocoder geocoder;
    LatLng latLng;
    Circle circle;

    private static int UPDATE_INTERVAL = 5000; // SEC
    private static int FATEST_INTERVAL = 3000; // SEC
    private static int DISPLACEMENT = 10; // METERS

    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 7172;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {//izinler tamsa?
                    if (checkPlayServices())
                        buildGoogleApiClient();
                    createLocationRequest();
                }
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (googleServicesAvailable()) {//google haritalar yüklü hata var mı
            setContentView(R.layout.activity_maps);//eğer ki haritaları açamıyorsa görüntü sağlanmayacak
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Run-time request permission
                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                }, MY_PERMISSION_REQUEST_CODE);
            } else {
                if (checkPlayServices()) {
                    buildGoogleApiClient();
                    createLocationRequest();
                }
            }

            //initmap
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
            mapFragment.getMapAsync(this);
            //onMapReady devreye gircek
            //


        }
        geocoder = new Geocoder(this, Locale.getDefault());


    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "bu telefon desteklenmiyor", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }
    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();


        mGoogleApiClient.connect();


    }
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);

    }
    public boolean googleServicesAvailable() {//play sevicese ulaşılabiliyor mu
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        }
        //eğer ki kullanıcıda google maps yoksa?
        else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Play Services'e bağlanılamıyor", Toast.LENGTH_LONG).show();
        }
        return false;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;//googleMap object dönecek
        displayLocation();

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                //save current location
                latLng = point;

                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final android.location.Address address;
                address = addresses.get(0);

                if (address != null) {
                    //
                }

                //remove previously placed Marker
                if (marker != null) {
                    marker.remove();
                }

                //place marker where user just clicked
                marker = mGoogleMap.addMarker(new MarkerOptions().position(point)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                if(circle!=null){
                    circle.setCenter(marker.getPosition());
                }


            }
        });
        editTextMeter = (EditText) findViewById(R.id.editTextKm);
        buttonMeter =(Button) findViewById(R.id.buttonMeter);
        buttonMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextMeter.getText().toString().trim().equals("")){
                    String stringMeter= editTextMeter.getText().toString();
                    int meter = Integer.parseInt(stringMeter);
                    LatLng ll = marker.getPosition();
                    drawCircle(ll,meter);
                }
            }
        });

    }

    private void drawCircle(LatLng ll, int meter) {


        if(circle!=null ){
            circle.setCenter(ll);
            circle.setRadius(meter);
        }


        else {

            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(ll)
                    .radius(meter)
                    .strokeColor(Color.BLACK)
                    .fillColor(R.color.marker_color)
                    .strokeWidth(2);

            circle = mGoogleMap.addCircle(circleOptions);
        }



    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //
        //
        // location güncellenmiyor
        //getlastlocation incele
        //
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            goToLocationZoom(latitude, longitude, 15);//haritalar başlatıldığında ilk neresini merkez alsın
            if(marker!=null){
                marker.remove();
            }
            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(latitude, longitude));

            marker = mGoogleMap.addMarker(options);//marker = mGoogleMap.addMarker(options) olmalı tek bir markerin olması için

            if(circle!=null){
                circle.setCenter(marker.getPosition());
            }

        }
    }


    private void goToLocationZoom(double lat, double lng, int zoom) {// latitude=enlem longitude =boylam
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);//görüş alanını güncelle ve zoomla
        mGoogleMap.moveCamera(update);

    }



    public void geoLocate(View view) throws IOException {
        //android:onClick="geoLocate" eklendi git butonuna
        EditText editTextWhereGo = (EditText) findViewById(R.id.editTextWhereGo);
        String location = editTextWhereGo.getText().toString();

        if(location.trim().equals("")){
            Toast.makeText(Maps.this,"Lütfen konum belirtininiz",Toast.LENGTH_LONG).show();
        }
        else {
            Geocoder gc = new Geocoder(this);//geo coder object geo kordinatları verecek,ana işi string alır ve enlem boylama dönüştürür
            List<android.location.Address> list = gc.getFromLocationName(location, 1);//1 sonuça ihtiyacımız var en faazla demek
            //kullanıcının girdiği yer için adres listi döndürür


            //Address tipinde olmalıydı dikkat et!!

            android.location.Address address = list.get(0);//ilk itemi al listede ki
            String locality = address.getLocality();


            double lat = address.getLatitude();
            double lng = address.getLongitude();
            goToLocationZoom(lat, lng, 15);

            if (marker != null) {
                marker.remove();
            }
            MarkerOptions options = new MarkerOptions()
                    .title(locality)
                    .position(new LatLng(lat, lng));
            marker = mGoogleMap.addMarker(options);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return super.onCreateOptionsMenu(menu);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mapTypeHybrid:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.mapTypeNormal:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        if(mRequestingLocationUpdates)
            startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
        if(mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void mapsActivityEnded(View view) {
        if(editTextMeter.getText().toString().trim().equals("") || Integer.parseInt(editTextMeter.getText().toString()) ==0){
            Toast.makeText(Maps.this,"Lütfen mesafeyi giriniz",Toast.LENGTH_LONG).show();
        }
        else{
            //Return an integer from edittext
            LatLng latLng = marker.getPosition();
            String stringMeter= editTextMeter.getText().toString();
            int meter = Integer.parseInt(stringMeter);

            Intent intent = new Intent();
            intent.putExtra("latLng",latLng);
            intent.putExtra("meter",meter);
            setResult(RESULT_OK,intent);
            finish();

        }

    }
}
