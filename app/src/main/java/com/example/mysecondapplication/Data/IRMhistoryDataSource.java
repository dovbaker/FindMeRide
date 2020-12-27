package com.example.mysecondapplication.Data;

import com.example.mysecondapplication.Entities.Travel;

import java.util.List;

public interface IRMhistoryDataSource {
    public void addTravel(Travel p);

    public void addTravel(List<Travel> travelList);

    public void editTravel(Travel p);

    public void deleteTravel(Travel p);

    public void clearTable();
}