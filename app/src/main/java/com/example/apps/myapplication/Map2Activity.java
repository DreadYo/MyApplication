package com.example.apps.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;

public class Map2Activity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;
    private Button btnFindPath;
    private EditText etOrigin, etDestination;
    private String strOrigin, strDestination;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        addListenerOnButton();
//////
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("TEST", "Place: " + place.getName());
                etOrigin.setText(place.getName().toString());
                Log.i("TEST","getAddress: " + place.getAddress());
                Log.i("TEST","toString: " + place.toString());
                Log.i("TEST","getAttributions: " + place.getAttributions());
                Log.i("TEST","getLocale: " + place.getLocale());
                Log.i("TEST","getPhoneNumber: " + place.getPhoneNumber());
                Log.i("TEST","getLatLng: " + place.getLatLng());

//                Place: Nemiga 3 Shopping Mall
//                getAddress: Ulitsa Nemiga 3, Minsk, Belarus
//                toString: PlaceEntity{id=ChIJq00kouvP20YRaLORPv2YyS8, placeTypes=[84, 1013, 34], locale=null, name=Nemiga 3 Shopping Mall, address=Ulitsa Nemiga 3, Minsk, Belarus, phoneNumber=+375 44 566-50-63, latlng=lat/lng: (53.903945500000006,27.552444899999998), viewport=LatLngBounds{southwest=lat/lng: (53.902735819708504,27.550826519708497), northeast=lat/lng: (53.9054337802915,27.5535244802915)}, websiteUri=http://nemiga3.by/, isPermanentlyClosed=false, priceLevel=-1}
//                getAttributions: null
//                getLocale: null
//                getPhoneNumber: +375 44 566-50-63
//                getLatLng: lat/lng: (53.903945500000006,27.552444899999998)
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TEST", "An error occurred: " + status);
            }
        });

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
//                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .setCountry("BY")
                .build();

        autocompleteFragment.setFilter(typeFilter);

//////////


    }

    public void addListenerOnButton() {
        btnFindPath = (Button)findViewById(R.id.btnFindPath);
        etOrigin = (EditText)findViewById(R.id.etOrigin);
        etDestination = (EditText)findViewById(R.id.etDestination);

        btnFindPath.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendRequest();
                    }
                }
        );
    }

    private void sendRequest() {
        String origin = etOrigin.getText().toString();
        String destination = etDestination.getText().toString();

        if (origin.isEmpty()) {
            Toast.makeText(Map2Activity.this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (destination.isEmpty()) {
            Toast.makeText(Map2Activity.this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng myWork = new LatLng(53.897248, 27.541629);
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .position(myWork)
                .title("Marker in My Work")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pushpin))
        ));

//        mMap.addPolyline(new PolylineOptions().add(
//                myWork,
//                new LatLng(53.887230, 27.514690),
//                new LatLng(53.886471, 27.538165),
//                new LatLng(53.917925, 27.555459)
//                )
//                        .width(10)
//                        .color(Color.RED)
//        );

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myWork, 18));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}
