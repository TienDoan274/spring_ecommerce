package com.eazybytes.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class LaptopRequest extends ProductRequest{

    // Bộ xử lý
    private String processorModel; // công nghệ cpu
    private String coreCount; // số core
    private String threadCount; // số luồng
    private String cpuSpeed; // tốc độ cpu
    private String maxCpuSpeed; // tốc độ tối đa

    //Bộ nhớ ram, ổ cứng
    private String ram;
    private String ramType;
    private String ramBusSpeed;
    private String maxRam;
    private String storage;

    //Màn hình
    private String screenSize;
    private String resolution;
    private String refreshRate;// tần số quét
    private String colorGamut; // độ phủ màu
    private List<String> displayTechnology; // công nghệ màn hình

    //Đồ họa và âm thanh
    private String graphicCard; // card màn hình
    private String audioTechnology; // công nghệ âm thanh
    private List<String> ports; // cổng giao tiếp
    private List<String> wirelessConnectivity; // kết nối không dây
    private String webcam;
    private List<String> otherFeatures;
    private String keyboardBacklight;

    //Kích thước - khối lượng - pin
    private String size;
    private String material;
    private String battery;
    private String OS;
}
