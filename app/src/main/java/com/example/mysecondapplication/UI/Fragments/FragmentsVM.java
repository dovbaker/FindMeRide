package com.example.mysecondapplication.UI.Fragments;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.mysecondapplication.Data.Repository.ITravelRepository;
import com.example.mysecondapplication.Data.Repository.TravelRepository;
import com.example.mysecondapplication.Entities.Travel;

import java.util.LinkedList;
import java.util.List;

public class FragmentsVM extends AndroidViewModel {
  private Context context;
    private MutableLiveData<String> mText;
    ITravelRepository repository;
    public FragmentsVM(Application p) {
      super(p);
        mText = new MutableLiveData<>();
//        if (NavigationDrawerVM.super.getClass() instanceof  RegisteredTravels.class)
//        mText.setValue("This is RegisteredTravels fragment");
//        else
//            mText.setValue("This is not  RegisteredTravels fragment");
     //   context =p.getBaseContext();
        repository = TravelRepository.getInstance(p);
    }

    public LiveData<String> getText() {
        return mText;
    }
    public void addTravel(Travel travel)
    {
        repository.addTravel(travel);
    }
    public void updateTravel(Travel travel)
    {
        repository.updateTravel(travel);
    }

    public MutableLiveData<List<Travel>> getOpenTravels()
    {
        return (MutableLiveData<List<Travel>>) repository.getOpenTravels();
    }

    public MutableLiveData<List<Travel>> getUserTravels()
    {

//        MutableLiveData<List<Travel>> UserTravelList= repository.getUserTravels();
//        List<Travel> userTravelList= (List<Travel>) UserTravelList;
//        for (Travel t : UserTravelList)
//            if(t.getRequestType().equals(Travel.RequestType.close))
//                userTravelList.remove(t);
//        UserTravelList.setValue(userTravelList);
//        return UserTravelList;
        return repository.getUserTravels();
    }
    public MutableLiveData<List<Travel>> gethistoryTravels()
    {
        return (MutableLiveData<List<Travel>>) repository.getHistoryTravels();
    }
    public MutableLiveData<Boolean> getIsSuccess()
    {
        return repository.getIsSuccess();
    }
}
