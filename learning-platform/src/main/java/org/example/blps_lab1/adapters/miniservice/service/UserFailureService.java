package org.example.blps_lab1.adapters.miniservice.service;


import org.example.blps_lab1.adapters.miniservice.model.UserFailure;

public interface UserFailureService {

    void saveFailedUser(UserFailure userFailure);
    void recoverAll();

}
