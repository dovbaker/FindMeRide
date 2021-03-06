package com.example.mysecondapplication.UI.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapplication.Entities.Travel;
import com.example.mysecondapplication.R;
import com.example.mysecondapplication.UI.recyclerView.ListAdapterCompany;

import java.util.LinkedList;
import java.util.List;

/**
 * the travels requests relevant to the specific company that is now logged in the app.
 * displays the travel requests that are in open and sent status.
 */
public class CompanyTravels extends Fragment {

    private FragmentsVM fragmentsVM;
    Location location;
    LocationManager locationManager ;
    LocationListener locationListener;
    double curLatitude; //the current location latitude
    double curLongitude;//the current location longtitude
    Context contex;
    RecyclerView recyclerView;
    ListAdapterCompany adapter;
    public List<Travel> Travels;
    public int    maxDis=30000; //the maximum distance from user to ride location to display.

    /**
     * create an instance of view model
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentsVM =
                new ViewModelProvider(this).get(FragmentsVM.class);
        View root = inflater.inflate(R.layout.fragment_company_travels, container, false);
         contex =this.getActivity();
         //active locationManager to find current location
         locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        //the location listener updates every time the user changes location
         locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                 curLatitude = location.getLatitude();
                 curLongitude = location.getLongitude();
            }


            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

       getLocation();
       //check Permission to find current location
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);

        }
        //find current location
         location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
         curLatitude=location.getLatitude();
         curLongitude=location.getLongitude();

         contex =CompanyTravels.this.getActivity().getBaseContext();
        recyclerView = (RecyclerView) root.findViewById(R.id.company_travel_recyclerView);
        //make lines between layout in list
        DividerItemDecoration itemDecor = new DividerItemDecoration(CompanyTravels.this.getActivity(),1);
        recyclerView.addItemDecoration(itemDecor);

        //  get all the travels that open for suggestions into grafic list
        //  react to changes
        fragmentsVM.getOpenTravels(curLatitude,curLongitude,maxDis).observe(getViewLifecycleOwner(), new Observer<List<Travel>>() {
            @Override
            public void onChanged(List<Travel> travels) {
                Travels=new LinkedList<>(travels);
                Travel[] travelsArr = new Travel[travels.size()];
                travels.toArray(travelsArr);
                adapter = new ListAdapterCompany(travelsArr, contex,requireActivity());
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(CompanyTravels.this.getActivity()));
                recyclerView.setAdapter(adapter);
            }
        });

        return root;
    }

    /**
     * check approval to use GPS
     * activate listener with accuracy parameters
     */
        public void getLocation()
            {

                //     Check the SDK version and whether the permission is already granted or not.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this.getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);

                } else {
                    // Android version is lesser than 6.0 or the permission is already granted.
                    //active locationListener

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, locationListener);
                }
            }
    }
