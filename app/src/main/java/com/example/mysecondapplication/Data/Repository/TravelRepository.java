package com.example.mysecondapplication.Data.Repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.mysecondapplication.Data.RMhistoryDataSource;
import com.example.mysecondapplication.Data.IRMhistoryDataSource;
import com.example.mysecondapplication.Data.IFBtravelDataSource;
import com.example.mysecondapplication.Data.FBtravelDataSource;
import com.example.mysecondapplication.Entities.Travel;

import java.util.List;

public class TravelRepository implements ITravelRepository {
    IFBtravelDataSource iFBtravelDataSource;
    private IRMhistoryDataSource iRMhistoryDataSource;


    private MutableLiveData<List<Travel>> mutableLiveData = new MutableLiveData<>();



    private static TravelRepository instance;
    public static TravelRepository getInstance(Application application) {
        if (instance == null)
            instance = new TravelRepository(application);
        return instance;
    }

    private TravelRepository(Application application) {
        iFBtravelDataSource = FBtravelDataSource.getInstance();
        iRMhistoryDataSource = new RMhistoryDataSource(application.getApplicationContext());

        IFBtravelDataSource.NotifyToTravelListListener notifyToTravelListListener = new IFBtravelDataSource.NotifyToTravelListListener() {
            @Override
            public void onTravelsChanged() {
                List<Travel> travelList = iFBtravelDataSource.getAllTravels();
                mutableLiveData.setValue(travelList);

                iRMhistoryDataSource.clearTable();
                iRMhistoryDataSource.addTravel(travelList);

            }
        };

        iFBtravelDataSource.setNotifyToTravelListListener(notifyToTravelListListener);
    }

    @Override
    public void addTravel(Travel travel) {
        iFBtravelDataSource.addTravel(travel);
    }

    @Override
    public void updateTravel(Travel travel) {
        iFBtravelDataSource.updateTravel(travel);
    }

    @Override
    public MutableLiveData<List<Travel>> getAllTravels() {
        return mutableLiveData;
    }

    @Override
    public MutableLiveData<Boolean> getIsSuccess() {
        return iFBtravelDataSource.getIsSuccess();
    }
}