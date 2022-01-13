package com.apps.onlinegroceriesapps.activity.screen;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.apps.onlinegroceriesapps.R;
import com.apps.onlinegroceriesapps.activity.MainActivity;
import com.apps.onlinegroceriesapps.adapter.AddressListAdapter;
import com.apps.onlinegroceriesapps.api.ApiClient;
import com.apps.onlinegroceriesapps.api.CommanApiCall.LoginByPhoneApiCall;
import com.apps.onlinegroceriesapps.api.CommanApiCall.UserProfileCommonApiCall;
import com.apps.onlinegroceriesapps.api.CommanApiCall.interfaces.LoginByPhoneApiCallInterfaces;
import com.apps.onlinegroceriesapps.api.network.services.LoginPhoneApiInterfaces;
import com.apps.onlinegroceriesapps.databinding.ActivityAddressSelectBinding;
import com.apps.onlinegroceriesapps.models.CommonGlobalMessageModel;
import com.apps.onlinegroceriesapps.models.UserAddressList;
import com.apps.onlinegroceriesapps.models.UserModel;
import com.apps.onlinegroceriesapps.utils.Constent;
import com.apps.onlinegroceriesapps.utils.GenericDelay;
import com.apps.onlinegroceriesapps.utils.GpsAddress;
import com.apps.onlinegroceriesapps.utils.LoadingSpinner;
import com.apps.onlinegroceriesapps.utils.UserPrefs;
import com.apps.onlinegroceriesapps.utils.UserResponseHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;


public class AddressSelectActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener {

    private static final String TAG = "AddressSelectActivity";
    private GoogleMap mMap;
    private ActivityAddressSelectBinding binding;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    FusedLocationProviderClient fusedLocationProviderClient;
    Geocoder geocoder;
    Marker userLocationMarker;
    LocationRequest locationRequest;
    double latitude, longitude;
    Circle userLocationAccuracyCircle;
    private LoginPhoneApiInterfaces apiService;
    LoadingSpinner loadingSpinner;
    UserResponseHelper helper;
    UserPrefs userPrefs;
    UserModel userModel ;
    AddressListAdapter addressListAdapter;
    UserAddressList userAddressList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddressSelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiService = ApiClient.getClient().create(LoginPhoneApiInterfaces.class);
        loadingSpinner = new LoadingSpinner(this);
        helper = new UserResponseHelper(this);
        userPrefs = new UserPrefs(this);
        userModel = userPrefs.getAuthPreferenceObjectJson(UserPrefs.users);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


//        Geocoder geocoder;
//        List<Address> addresses;
//        geocoder = new Geocoder(this, Locale.getDefault());
//
//        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//
//        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//        String city = addresses.get(0).getLocality();
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
//        String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName(); //


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Context context;
        geocoder = new Geocoder(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);




        userAddressList();
        Click();

        //TODO: add more optimized code for save data or address in server for now its just OK.
        binding.addressContnue.setOnClickListener(view -> {
            new LoginByPhoneApiCall(this, new LoginByPhoneApiCallInterfaces() {
                @Override
                public void onResult(CommonGlobalMessageModel body) {
                    new UserPrefs(AddressSelectActivity.this).setAddress("yes");
                    helper.showSuccess(body.getMessage());
                    new GenericDelay(() -> {
                        startActivity(new Intent(AddressSelectActivity.this, MainActivity.class));
                    }).Use();
                }
            }).updateUserAddress(loadingSpinner,apiService,userAddressList);
        });
    }

    private void Click() {
        binding.addressHead.setOnClickListener(v -> {
            binding.gps.setVisibility(View.GONE);
            binding.checkbox.setVisibility(View.VISIBLE);
            userAddressList();
        });
        binding.fullAddress.setOnClickListener(v -> {
            binding.gps.setVisibility(View.GONE);
            binding.checkbox.setVisibility(View.VISIBLE);
            userAddressList();
        });
        binding.gps.setOnClickListener(v -> {
            binding.gps.setVisibility(View.GONE);
            binding.checkbox.setVisibility(View.VISIBLE);
            userAddressList();
        });
    }

    private void userAddressList() {
        new UserProfileCommonApiCall(this, new UserProfileCommonApiCall.CallBack() {
            @Override
            public void onGetAddresses(List<UserAddressList> body) {

                addressListAdapter = new AddressListAdapter(AddressSelectActivity.this, body, helper, new AddressListAdapter.AddressListInterfaces() {
                    @Override
                    public void getAddressList(UserAddressList data) {
                        userPrefs.setDefaultAddress(data);
                        userAddressList = data;
//                        getAddressList.getAddressList(data);
                        binding.gps.setVisibility(View.VISIBLE);
                        binding.checkbox.setVisibility(View.GONE);
                    }
                });


                GridLayoutManager horizontalLayoutManager
                        = new GridLayoutManager(AddressSelectActivity.this, 1, GridLayoutManager.VERTICAL, false);
                binding.address.setLayoutManager(horizontalLayoutManager);
//        binding.exclusiveOffer.setNestedScrollingEnabled(true);
//                binding.address.addItemDecoration( new LayoutMarginDecoration( 2,  Util.convertDpToPx(context, 10)) );
                binding.address.setAdapter(addressListAdapter);
            }
        }).getUserAddressesList(loadingSpinner,userModel);

    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerDragListener(this);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            enableUserLocation();
//            zoomToUserLocation();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We can show user a dialog why this permission is necessary
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            }

        }

        // Add a marker at Taj Mahal and move the camera
//        LatLng latLng = new LatLng(27.1751, 78.0421);
//        MarkerOptions markerOptions = new MarkerOptions()
//                                            .position(latLng)
//                                            .title("Taj Mahal")
//                                            .snippet("Wonder of the world!");
//        mMap.addMarker(markerOptions);
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
//        mMap.animateCamera(cameraUpdate);

        try {
            List<Address> addresses = geocoder.getFromLocationName(Constent.DEFAULT_LOCATION_NAME, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                LatLng london = new LatLng(address.getLatitude(), address.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(london)
                        .title(address.getLocality());
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(london, 16));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //old
//        mMap = googleMap;
//
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.setOnMapLongClickListener(this);
//        mMap.setOnMarkerDragListener(this);
//
//        // Add a marker in Sydney and move the camera
//
//        LatLng sydney = new LatLng(26.33236021748644, 86.06675662437607);
//
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d(TAG, "onLocationResult: " + locationResult.getLastLocation());
            if (mMap != null) {
                setUserLocationMarker(locationResult.getLastLocation());
            }
        }
    };

    private void setUserLocationMarker(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        setGpsLocation(location.getLatitude(),location.getLongitude());
        if (userLocationMarker == null) {
            //Create a new marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.super_man));
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float) 0.5, (float) 0.5);
            userLocationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 117));
        } else {
            //use the previously created marker
            userLocationMarker.setPosition(latLng);
            userLocationMarker.setRotation(location.getBearing());

//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getMinZoomLevel()));
        }

        if (userLocationAccuracyCircle == null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.strokeWidth(4);
            circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
            circleOptions.fillColor(Color.argb(32, 255, 0, 0));
            circleOptions.radius(location.getAccuracy());
            userLocationAccuracyCircle = mMap.addCircle(circleOptions);
        } else {
            userLocationAccuracyCircle.setCenter(latLng);
            userLocationAccuracyCircle.setRadius(location.getAccuracy());
        }
    }

    private void setGpsLocation(double latitude, double longitude) {
        Address gps_address = new GpsAddress(latitude,longitude,AddressSelectActivity.this).access();
        binding.addressHead.setText(gps_address.getFeatureName() );
        binding.fullAddress.setText(gps_address.getAddressLine(0));
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_LOCATION_REQUEST_CODE
            );
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }

    private void stopLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        }  // you need to request permissions...

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

    }

    private void zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
//                mMap.addMarker(new MarkerOptions().position(latLng));
                }
            });
        }

    }


    @Override
    public void onMarkerDrag(@NonNull Marker marker) {
        Log.d(TAG, "onMarkerDragStart: ");
    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {

        LatLng latLng = marker.getPosition();
        setGpsLocation(latLng.latitude,latLng.longitude);
        Log.d(TAG, "onMarkerDragEnd: " + latLng.latitude + " ==== " + latLng.longitude);
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                marker.setTitle(streetAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {
        Log.d(TAG, "onMarkerDrag: ");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                zoomToUserLocation();
            } else {
                //We can show a dialog that permission is not granted...
            }
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Log.d(TAG, "onMapLongClick: " + latLng.toString());
        setGpsLocation(latLng.latitude,latLng.longitude);
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(streetAddress)
                        .draggable(true)
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        setGpsLocation(latLng.latitude,latLng.longitude);
        Log.d(TAG, "onMapLongClick: " + latLng.toString());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(streetAddress)
                        .draggable(true)
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}