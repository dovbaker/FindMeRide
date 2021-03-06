package com.example.mysecondapplication.UI.NavigationDrawer;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mysecondapplication.Entities.Travel;
import com.example.mysecondapplication.Entities.UserLocation;
import com.example.mysecondapplication.*;
import com.example.mysecondapplication.UI.Fragments.FragmentsVM;
import com.example.mysecondapplication.UI.Fragments.RegisteredTravels;
import com.example.mysecondapplication.UI.Login_Activity.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Date;
import java.util.HashMap;

/**
 * tha main screen that can go to all the fragments
 */
public class NavigationDrawer extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public TextView Txt_welcomeUser;
    private String email="";
    public FirebaseAuth mAuth;
    private FragmentsVM fragmentsVM;
    RegisteredTravels registeredTravels;
    LocationManager locationManager;
    LocationListener locationListener;
    @Override
    /**
     * set the view of the screen to the user
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        fragmentsVM = new ViewModelProvider(this).get(FragmentsVM.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        mAuth = FirebaseAuth.getInstance();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//go beck to home page
               // mAuth.signOut();
                Intent i = new Intent(NavigationDrawer.this, LoginActivity.class);
                startActivity(i);
            }
        });
        //    register the  Receiver to the app and tha wanted action
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction("com.javacodegeeks.android.A_CUSTOM_INTENT");
        registerReceiver(new MyBroadcastReceiver(), intentFilter);

        startService(new Intent(NavigationDrawer.this, myService.class));

         registeredTravels =new RegisteredTravels();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_RegisteredTravels, R.id.nav_CompanyTravels, R.id.nav_History_Travels)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //set welcome message to specific user
        Bundle extras = getIntent().getExtras();
        if (extras != null)
             email = extras.getString("email");
             email=email.split("@")[0];
         Txt_welcomeUser= findViewById(R.id.Txt_welcome_user);
         Txt_welcomeUser.setText("welcome user: " + email);

       //  checkdate();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                double curLatitude = location.getLatitude();
                double curLongitude = location.getLongitude();
            }
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            public void onProviderEnabled(String provider) { }
            public void onProviderDisabled(String provider) { }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void helpmassege(MenuItem item) {//open Dialog with help instructions
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setTitle("instructions");
        dlgAlert.setMessage("hello,\n" +
                "\ncustomer- you will be able to see your travels and confirm companies that have offered themselves for the trip.\n" +
                "\nshuttle company - you can see the open trips in your area and offer your services to them.\n" +
                "\napp manager - you can see the trips that ended and did not pay for the app and contact them.");
        dlgAlert.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { }    } );
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();


    }
      public void exit(MenuItem item){
          finish();
     //     System.exit(0);
          moveTaskToBack(true);
    }
    public void checkPassword(MenuItem item){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                               if (userInput.getText().toString()=="1")
                                   Toast.makeText(NavigationDrawer.this, "good",
                                           Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
    // add data to check the app
    public void checkdate () {

        fragmentsVM = new ViewModelProvider(this).get(FragmentsVM.class);
        fragmentsVM.getIsSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean t) {
                if (t)
                    Toast.makeText(NavigationDrawer.this, "Data Inserted", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(NavigationDrawer.this, "Data Not Inserted", Toast.LENGTH_LONG).show();

            }
        });
        try {
            String travelDate ;
            travelDate =  "2020"+"-"+"02"+"-"+"25";
            String  travelDate2 =  "2020"+"-"+"02"+"-"+"30";
            String  travelDate3 =  "2020"+"-"+"02"+"-"+"28";
            Date tDate = new Travel.DateConverter().fromTimestamp(travelDate);
            Date tDate2 = new Travel.DateConverter().fromTimestamp(travelDate2);
            Date tDate3 = new Travel.DateConverter().fromTimestamp(travelDate3);
            if (tDate == null)
                throw new Exception("date error");

            Travel travel1 = new Travel();
            travel1.setClientName("abi");
            travel1.setClientPhone("026334512");
            travel1.setClientEmail("ddkill8@gmail.com");
            travel1.setPickupAddress(new UserLocation(	31.934466609645973, 35.02629946297578));
            travel1.setDetentionAddress(new UserLocation(31.776873932302315, 35.23451923131213));
            travel1.setTravelDate(tDate);
            travel1.setArrivalDate(tDate2);
            travel1.setRequestType(Travel.RequestType.sent);
            travel1.setCompany(new HashMap<String, Boolean>());
            travel1.getCompany().put("Egged",Boolean.FALSE);
            travel1.getCompany().put("TsirTour",Boolean.FALSE);
            travel1.setVIPBUS(true);

            fragmentsVM.addTravel(travel1);
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}
