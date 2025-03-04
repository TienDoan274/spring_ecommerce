package com.eazybytes.service;

import com.eazybytes.dto.ProductRequest;
import com.eazybytes.dto.ProductResponse;
import com.eazybytes.dto.PhoneRequest;
import com.eazybytes.dto.LaptopRequest;
import com.eazybytes.dto.PhoneResponse;
import com.eazybytes.dto.LaptopResponse;
import com.eazybytes.model.Product;
import com.eazybytes.model.Phone;
import com.eazybytes.model.Laptop;
import com.eazybytes.repository.LaptopRepository;
import com.eazybytes.repository.PhoneRepository;
import com.eazybytes.repository.ProductRepository;
import com.eazybytes.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final PhoneRepository phoneRepository;
    private final LaptopRepository laptopRepository;

    public PhoneResponse createPhone(PhoneRequest phoneRequest) {
        Phone phone = new Phone();
        // Thiết lập các trường cơ bản của Product
        phone.setName(phoneRequest.getName());
        phone.setDescription(phoneRequest.getDescription());

        phone.setBrand(phoneRequest.getBrand());
        phone.setImages(phoneRequest.getImages());
        phone.setIsAvailable(true);
        phone.setWarrantyPeriod(phoneRequest.getWarrantyPeriod());
        phone.setProductReviews(phoneRequest.getProductReviews());
        phone.setPromotions(phoneRequest.getPromotions());
        phone.setRelease(phoneRequest.getRelease());

        // Thiết lập các trường riêng của Phone
        phone.setOs(phoneRequest.getOs());
        phone.setProcessor(phoneRequest.getProcessor());
        phone.setCpuSpeed(phoneRequest.getCpuSpeed());
        phone.setGpu(phoneRequest.getGpu());
        phone.setRam(phoneRequest.getRam());
        phone.setStorage(phoneRequest.getStorage());
        phone.setAvailableStorage(phoneRequest.getAvailableStorage());
        phone.setContactLimit(phoneRequest.getContactLimit());

        // Camera và màn hình
        phone.setRearCameraResolution(phoneRequest.getRearCameraResolution());
        phone.setRearVideoRecording(phoneRequest.getRearVideoRecording());
        phone.setRearFlash(phoneRequest.getRearFlash());
        phone.setRearCameraFeatures(phoneRequest.getRearCameraFeatures());
        phone.setFrontCameraResolution(phoneRequest.getFrontCameraResolution());
        phone.setFrontCameraFeatures(phoneRequest.getFrontCameraFeatures());
        phone.setDisplayTechnology(phoneRequest.getDisplayTechnology());
        phone.setDisplayResolution(phoneRequest.getDisplayResolution());
        phone.setScreenSize(phoneRequest.getScreenSize());
        phone.setMaxBrightness(phoneRequest.getMaxBrightness());
        phone.setScreenProtection(phoneRequest.getScreenProtection());

        // Pin và sạc
        phone.setBatteryCapactity(phoneRequest.getBatteryCapactity());
        phone.setBatteryType(phoneRequest.getBatteryType());
        phone.setMaxChargingPower(phoneRequest.getMaxChargingPower());
        phone.setBatteryFeatures(phoneRequest.getBatteryFeatures());

        // Tiện ích
        phone.setSecurityFeatures(phoneRequest.getSecurityFeatures());
        phone.setSpecialFeatures(phoneRequest.getSpecialFeatures());
        phone.setWaterResistance(phoneRequest.getWaterResistance());
        phone.setRecording(phoneRequest.getRecording());
        phone.setVideo(phoneRequest.getVideo());
        phone.setAudio(phoneRequest.getAudio());

        // Kết nối
        phone.setMobileNetwork(phoneRequest.getMobileNetwork());
        phone.setSimType(phoneRequest.getSimType());
        phone.setWifi(phoneRequest.getWifi());
        phone.setGps(phoneRequest.getGps());
        phone.setBluetooth(phoneRequest.getBluetooth());
        phone.setChargingPort(phoneRequest.getChargingPort());
        phone.setHeadphoneJack(phoneRequest.getHeadphoneJack());
        phone.setOtherConnectivity(phoneRequest.getOtherConnectivity());

        // Thiết kế
        phone.setDesignType(phoneRequest.getDesignType());
        phone.setMaterials(phoneRequest.getMaterials());
        phone.setSizeWeight(phoneRequest.getSizeWeight());

        phone.setColors(phoneRequest.getColors());

        Phone savedPhone = (Phone) phoneRepository.save(phone);
        log.info("Phone {} is saved", savedPhone.getId());
        return mapToPhoneResponse(savedPhone);
    }

    public LaptopResponse createLaptop(LaptopRequest laptopRequest) {
        Laptop laptop = new Laptop();
        // Thiết lập các trường cơ bản của Product
        laptop.setName(laptopRequest.getName());
        laptop.setDescription(laptopRequest.getDescription());

        laptop.setBrand(laptopRequest.getBrand());
        laptop.setImages(laptopRequest.getImages());
        laptop.setIsAvailable(true);
        laptop.setWarrantyPeriod(laptopRequest.getWarrantyPeriod());
        laptop.setProductReviews(laptopRequest.getProductReviews());
        laptop.setPromotions(laptopRequest.getPromotions());
        laptop.setRelease(laptopRequest.getRelease());

        // Thiết lập các trường riêng của Laptop
        laptop.setProcessorModel(laptopRequest.getProcessorModel());
        laptop.setCoreCount(laptopRequest.getCoreCount());
        laptop.setThreadCount(laptopRequest.getThreadCount());
        laptop.setCpuSpeed(laptopRequest.getCpuSpeed());
        laptop.setMaxCpuSpeed(laptopRequest.getMaxCpuSpeed());

        // RAM, ổ cứng
        laptop.setRam(laptopRequest.getRam());
        laptop.setRamType(laptopRequest.getRamType());
        laptop.setRamBusSpeed(laptopRequest.getRamBusSpeed());
        laptop.setMaxRam(laptopRequest.getMaxRam());
        laptop.setStorage(laptopRequest.getStorage());

        // Màn hình
        laptop.setScreenSize(laptopRequest.getScreenSize());
        laptop.setResolution(laptopRequest.getResolution());
        laptop.setRefreshRate(laptopRequest.getRefreshRate());
        laptop.setColorGamut(laptopRequest.getColorGamut());
        laptop.setDisplayTechnology(laptopRequest.getDisplayTechnology());

        // Đồ họa và âm thanh
        laptop.setGraphicCard(laptopRequest.getGraphicCard());
        laptop.setAudioTechnology(laptopRequest.getAudioTechnology());
        laptop.setPorts(laptopRequest.getPorts());
        laptop.setWirelessConnectivity(laptopRequest.getWirelessConnectivity());
        laptop.setWebcam(laptopRequest.getWebcam());
        laptop.setOtherFeatures(laptopRequest.getOtherFeatures());
        laptop.setKeyboardBacklight(laptopRequest.getKeyboardBacklight());

        // Kích thước - khối lượng - pin
        laptop.setSize(laptopRequest.getSize());
        laptop.setMaterial(laptopRequest.getMaterial());
        laptop.setBattery(laptopRequest.getBattery());
        laptop.setOs(laptopRequest.getOs());

        Laptop savedLaptop = (Laptop) laptopRepository.save(laptop);
        log.info("Laptop {} is saved", savedLaptop.getId());
        return mapToLaptopResponse(savedLaptop);
    }

    public List<PhoneResponse> getAllPhones() {
        List<Phone> phones = phoneRepository.findByType("PHONE");
        return phones.stream()
                .map(this::mapToPhoneResponse)
                .collect(Collectors.toList());
    }

    public List<LaptopResponse> getAllLaptops() {
        List<Laptop> laptops = laptopRepository.findByType("LAPTOP");
        return laptops.stream()
                .map(this::mapToLaptopResponse)
                .collect(Collectors.toList());
    }

    public PhoneResponse getPhoneById(String id) {
        Phone phone = phoneRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Phone not found with id: " + id));

        return mapToPhoneResponse(phone);
    }

    public LaptopResponse getLaptopById(String id) {
        Laptop laptop = laptopRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Laptop not found with id: " + id));

        return mapToLaptopResponse(laptop);
    }

    public List<PhoneResponse> getPhonesByNames(String name) {
        List<Phone> phones = phoneRepository.findByNameContainingIgnoreCase(name);
        return phones.stream()
                .map(this::mapToPhoneResponse)
                .collect(Collectors.toList());
    }

    public List<LaptopResponse> getLaptopsByNames(String name) {
        List<Laptop> laptops = laptopRepository.findByNameContainingIgnoreCase(name);
        return laptops.stream()
                .map(this::mapToLaptopResponse)
                .collect(Collectors.toList());
    }

    public String deletePhone(String id) {
        Phone phone = phoneRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Phone not found with id: " + id));
        phoneRepository.delete(phone);
        log.info("Phone {} is deleted", id);
        return "Phone with id: " + id + " has been successfully deleted";

    }

    public String deleteLaptop(String id) {
        Laptop laptop = laptopRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Laptop not found with id: " + id));
        laptopRepository.delete(laptop);
        log.info("Laptop {} is deleted", id);
        return "Laptop with id: " + id + " has been successfully deleted";

    }

    private PhoneResponse mapToPhoneResponse(Phone phone) {
        return PhoneResponse.builder()
                // Map basic product fields
                .id(phone.getId())
                .name(phone.getName())
                .description(phone.getDescription())
                .brand(phone.getBrand())
                .images(phone.getImages())
                .isAvailable(phone.getIsAvailable())
                .type(phone.getType())
                .warrantyPeriod(phone.getWarrantyPeriod())
                .productReviews(phone.getProductReviews())
                .promotions(phone.getPromotions())
                .release(phone.getRelease())

                // Map phone-specific fields
                .os(phone.getOs())
                .processor(phone.getProcessor())
                .cpuSpeed(phone.getCpuSpeed())
                .gpu(phone.getGpu())
                .ram(phone.getRam())
                .storage(phone.getStorage())
                .availableStorage(phone.getAvailableStorage())
                .contactLimit(phone.getContactLimit())

                // Camera và màn hình
                .rearCameraResolution(phone.getRearCameraResolution())
                .rearVideoRecording(phone.getRearVideoRecording())
                .rearFlash(phone.getRearFlash())
                .rearCameraFeatures(phone.getRearCameraFeatures())
                .frontCameraResolution(phone.getFrontCameraResolution())
                .frontCameraFeatures(phone.getFrontCameraFeatures())
                .displayTechnology(phone.getDisplayTechnology())
                .displayResolution(phone.getDisplayResolution())
                .screenSize(phone.getScreenSize())
                .maxBrightness(phone.getMaxBrightness())
                .screenProtection(phone.getScreenProtection())

                // Pin và sạc
                .batteryCapactity(phone.getBatteryCapactity())
                .batteryType(phone.getBatteryType())
                .maxChargingPower(phone.getMaxChargingPower())
                .batteryFeatures(phone.getBatteryFeatures())

                // Tiện ích
                .securityFeatures(phone.getSecurityFeatures())
                .specialFeatures(phone.getSpecialFeatures())
                .waterResistance(phone.getWaterResistance())
                .recording(phone.getRecording())
                .video(phone.getVideo())
                .audio(phone.getAudio())

                // Kết nối
                .mobileNetwork(phone.getMobileNetwork())
                .simType(phone.getSimType())
                .wifi(phone.getWifi())
                .gps(phone.getGps())
                .bluetooth(phone.getBluetooth())
                .chargingPort(phone.getChargingPort())
                .headphoneJack(phone.getHeadphoneJack())
                .otherConnectivity(phone.getOtherConnectivity())

                // Thiết kế và chất lượng
                .designType(phone.getDesignType())
                .materials(phone.getMaterials())
                .sizeWeight(phone.getSizeWeight())
                .colors(phone.getColors())

                .build();
    }

    private LaptopResponse mapToLaptopResponse(Laptop laptop) {
        return LaptopResponse.builder()
                // Map basic product fields
                .id(laptop.getId())
                .name(laptop.getName())
                .description(laptop.getDescription())
                .brand(laptop.getBrand())
                .images(laptop.getImages())
                .isAvailable(laptop.getIsAvailable())
                .type(laptop.getType())
                .warrantyPeriod(laptop.getWarrantyPeriod())
                .productReviews(laptop.getProductReviews())
                .promotions(laptop.getPromotions())
                .release(laptop.getRelease())

                // Bộ xử lý
                .processorModel(laptop.getProcessorModel())
                .coreCount(laptop.getCoreCount())
                .threadCount(laptop.getThreadCount())
                .cpuSpeed(laptop.getCpuSpeed())
                .maxCpuSpeed(laptop.getMaxCpuSpeed())

                // Bộ nhớ ram, ổ cứng
                .ram(laptop.getRam())
                .ramType(laptop.getRamType())
                .ramBusSpeed(laptop.getRamBusSpeed())
                .maxRam(laptop.getMaxRam())
                .storage(laptop.getStorage())

                // Màn hình
                .screenSize(laptop.getScreenSize())
                .resolution(laptop.getResolution())
                .refreshRate(laptop.getRefreshRate())
                .colorGamut(laptop.getColorGamut())
                .displayTechnology(laptop.getDisplayTechnology())

                // Đồ họa và âm thanh
                .graphicCard(laptop.getGraphicCard())
                .audioTechnology(laptop.getAudioTechnology())
                .ports(laptop.getPorts())
                .wirelessConnectivity(laptop.getWirelessConnectivity())
                .webcam(laptop.getWebcam())
                .otherFeatures(laptop.getOtherFeatures())
                .keyboardBacklight(laptop.getKeyboardBacklight())

                // Kích thước - khối lượng - pin
                .size(laptop.getSize())
                .material(laptop.getMaterial())
                .battery(laptop.getBattery())
                .os(laptop.getOs())
                .build();
    }

    public PhoneResponse updatePhone(String id, PhoneRequest phoneRequest) {
        Phone phone = phoneRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Phone not found with id: " + id));

        // Thiết lập các trường cơ bản của Product
        phone.setName(phoneRequest.getName());
        phone.setDescription(phoneRequest.getDescription());

        phone.setBrand(phoneRequest.getBrand());
        phone.setImages(phoneRequest.getImages());
        phone.setIsAvailable(true);
        phone.setWarrantyPeriod(phoneRequest.getWarrantyPeriod());
        phone.setProductReviews(phoneRequest.getProductReviews());
        phone.setPromotions(phoneRequest.getPromotions());
        phone.setRelease(phoneRequest.getRelease());

        // Thiết lập các trường riêng của Phone
        phone.setOs(phoneRequest.getOs());
        phone.setProcessor(phoneRequest.getProcessor());
        phone.setCpuSpeed(phoneRequest.getCpuSpeed());
        phone.setGpu(phoneRequest.getGpu());
        phone.setRam(phoneRequest.getRam());
        phone.setStorage(phoneRequest.getStorage());
        phone.setAvailableStorage(phoneRequest.getAvailableStorage());
        phone.setContactLimit(phoneRequest.getContactLimit());

        // Camera và màn hình
        phone.setRearCameraResolution(phoneRequest.getRearCameraResolution());
        phone.setRearVideoRecording(phoneRequest.getRearVideoRecording());
        phone.setRearFlash(phoneRequest.getRearFlash());
        phone.setRearCameraFeatures(phoneRequest.getRearCameraFeatures());
        phone.setFrontCameraResolution(phoneRequest.getFrontCameraResolution());
        phone.setFrontCameraFeatures(phoneRequest.getFrontCameraFeatures());
        phone.setDisplayTechnology(phoneRequest.getDisplayTechnology());
        phone.setDisplayResolution(phoneRequest.getDisplayResolution());
        phone.setScreenSize(phoneRequest.getScreenSize());
        phone.setMaxBrightness(phoneRequest.getMaxBrightness());
        phone.setScreenProtection(phoneRequest.getScreenProtection());

        // Pin và sạc
        phone.setBatteryCapactity(phoneRequest.getBatteryCapactity());
        phone.setBatteryType(phoneRequest.getBatteryType());
        phone.setMaxChargingPower(phoneRequest.getMaxChargingPower());
        phone.setBatteryFeatures(phoneRequest.getBatteryFeatures());

        // Tiện ích
        phone.setSecurityFeatures(phoneRequest.getSecurityFeatures());
        phone.setSpecialFeatures(phoneRequest.getSpecialFeatures());
        phone.setWaterResistance(phoneRequest.getWaterResistance());
        phone.setRecording(phoneRequest.getRecording());
        phone.setVideo(phoneRequest.getVideo());
        phone.setAudio(phoneRequest.getAudio());

        // Kết nối
        phone.setMobileNetwork(phoneRequest.getMobileNetwork());
        phone.setSimType(phoneRequest.getSimType());
        phone.setWifi(phoneRequest.getWifi());
        phone.setGps(phoneRequest.getGps());
        phone.setBluetooth(phoneRequest.getBluetooth());
        phone.setChargingPort(phoneRequest.getChargingPort());
        phone.setHeadphoneJack(phoneRequest.getHeadphoneJack());
        phone.setOtherConnectivity(phoneRequest.getOtherConnectivity());

        // Thiết kế
        phone.setDesignType(phoneRequest.getDesignType());
        phone.setMaterials(phoneRequest.getMaterials());
        phone.setSizeWeight(phoneRequest.getSizeWeight());

        phone.setColors(phoneRequest.getColors());

        Phone updatedPhone = phoneRepository.save(phone);
        log.info("Phone {} is updated", updatedPhone.getId());
        return mapToPhoneResponse(updatedPhone);
    }

    public LaptopResponse updateLaptop(String id, LaptopRequest laptopRequest) {
        Laptop laptop = laptopRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Laptop not found with id: " + id));

        laptop.setName(laptopRequest.getName());
        laptop.setDescription(laptopRequest.getDescription());

        laptop.setBrand(laptopRequest.getBrand());
        laptop.setImages(laptopRequest.getImages());
        laptop.setIsAvailable(true);
        laptop.setWarrantyPeriod(laptopRequest.getWarrantyPeriod());
        laptop.setProductReviews(laptopRequest.getProductReviews());
        laptop.setPromotions(laptopRequest.getPromotions());
        laptop.setRelease(laptopRequest.getRelease());

        // Thiết lập các trường riêng của Laptop
        laptop.setProcessorModel(laptopRequest.getProcessorModel());
        laptop.setCoreCount(laptopRequest.getCoreCount());
        laptop.setThreadCount(laptopRequest.getThreadCount());
        laptop.setCpuSpeed(laptopRequest.getCpuSpeed());
        laptop.setMaxCpuSpeed(laptopRequest.getMaxCpuSpeed());

        // RAM, ổ cứng
        laptop.setRam(laptopRequest.getRam());
        laptop.setRamType(laptopRequest.getRamType());
        laptop.setRamBusSpeed(laptopRequest.getRamBusSpeed());
        laptop.setMaxRam(laptopRequest.getMaxRam());
        laptop.setStorage(laptopRequest.getStorage());

        // Màn hình
        laptop.setScreenSize(laptopRequest.getScreenSize());
        laptop.setResolution(laptopRequest.getResolution());
        laptop.setRefreshRate(laptopRequest.getRefreshRate());
        laptop.setColorGamut(laptopRequest.getColorGamut());
        laptop.setDisplayTechnology(laptopRequest.getDisplayTechnology());

        // Đồ họa và âm thanh
        laptop.setGraphicCard(laptopRequest.getGraphicCard());
        laptop.setAudioTechnology(laptopRequest.getAudioTechnology());
        laptop.setPorts(laptopRequest.getPorts());
        laptop.setWirelessConnectivity(laptopRequest.getWirelessConnectivity());
        laptop.setWebcam(laptopRequest.getWebcam());
        laptop.setOtherFeatures(laptopRequest.getOtherFeatures());
        laptop.setKeyboardBacklight(laptopRequest.getKeyboardBacklight());

        // Kích thước - khối lượng - pin
        laptop.setSize(laptopRequest.getSize());
        laptop.setMaterial(laptopRequest.getMaterial());
        laptop.setBattery(laptopRequest.getBattery());
        laptop.setOs(laptopRequest.getOs());

        Laptop updatedLaptop = laptopRepository.save(laptop);
        log.info("Laptop {} is updated", updatedLaptop.getId());
        return mapToLaptopResponse(updatedLaptop);
    }
}