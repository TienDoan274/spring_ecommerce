package com.eazybytes.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor

public class WirelessEarphoneRequest extends ProductRequest{
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
}
