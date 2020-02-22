package com.example.android.boardingpass;

/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.boardingpass.databinding.ActivityMainBinding;
import com.example.android.boardingpass.utilities.FakeDataUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.example.android.boardingpass.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {

    // (3) Create a data binding instance called mBinding of type ActivityMainBinding
    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);


        // (4) Set the Content View using DataBindingUtil to the activity_main layout
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // (5) Load a BoardingPassInfo object with fake data using FakeDataUtils
        BoardingPassInfo info = FakeDataUtils.generateFakeBoardingPassInfo();
        mBinding.setInfo(info);

        // (9) Call displayBoardingPassInfo and pass the fake BoardingInfo instance
        //displayBoardingPassInfo(info);

    }

    private void displayBoardingPassInfo(BoardingPassInfo info) {

        // (6) Use mBinding to set the Text in all the textViews using the data in info
        mBinding.setVariable(R.id.textViewPassengerName, info.passengerName);
        mBinding.setVariable(R.id.textViewOriginAirport, info.originCode);
        mBinding.setVariable(R.id.textViewDestinationAirport, info.destCode);
        mBinding.setVariable(R.id.textViewFlightCode, info.flightCode);
        mBinding.setVariable(R.id.textViewTerminal, info.departureTerminal);
        mBinding.setVariable(R.id.textViewGate, info.departureGate);
        mBinding.setVariable(R.id.textViewSeat, info.seatNumber);

        // (7) Use a SimpleDateFormat formatter to set the formatted value in time text views
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.timeFormat), Locale.getDefault());
        mBinding.setVariable(R.id.textViewBoardingTime, simpleDateFormat.format(info.boardingTime));
        mBinding.setVariable(R.id.textViewDepartureTime, simpleDateFormat.format(info.departureTime));
        mBinding.setVariable(R.id.textViewArrivalTime, simpleDateFormat.format(info.arrivalTime));

        // (8) Use TimeUnit methods to format the total minutes until boarding
        long minutesUntilBoarding = info.getMinutesUntilBoarding();
        long hoursUntilBoarding = TimeUnit.HOURS.toHours(minutesUntilBoarding);
        long minutesLeftMinusHours = minutesUntilBoarding - TimeUnit.HOURS.toMinutes(hoursUntilBoarding);

        String hoursAndMintusUntilBoarding = getString(R.string.countDownFormat, hoursUntilBoarding, minutesLeftMinusHours);
        mBinding.setVariable(R.id.textViewBoardingInCountdown, hoursAndMintusUntilBoarding);

    }
}

