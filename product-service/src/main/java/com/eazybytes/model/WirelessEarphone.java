package com.eazybytes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@SuperBuilder
public class WirelessEarphone extends BaseProduct{
    private String batteryLife;
    private String chargingCaseBatteryLife;
    private String chargingPort;
    private String audioTechnology;

    private List<String> compatibility;
    private String connectionApp;
    private List<String> features;

    private String simultaneousConnections;
    private String connectionTechnology;
    private String controlType;

    private List<String> controlButtons;

    private String size;
    private String weight;
    private String brandOrigin;
    private String manufactured;

    public WirelessEarphone(){
        setType("WIRELESS_EARPHONE");
    }
}
