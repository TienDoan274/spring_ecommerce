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
        phone.setOriginalPrice(phoneRequest.getOriginalPrice());
        phone.setCurrentPrice(phoneRequest.getCurrentPrice());
        phone.setBrand(phoneRequest.getBrand());
        phone.setImage(phoneRequest.getImage());
        phone.setStockQuantity(phoneRequest.getStockQuantity());
        phone.setIsAvailable(true);
        phone.setWarrantyPeriod(phoneRequest.getWarrantyPeriod());
        phone.setProductReview(phoneRequest.getProductReview());
        phone.setPromotion(phoneRequest.getPromotion());
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
        phone.setDisplayResolutiona(phoneRequest.getDisplayResolutiona());
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

        phone.setColor(phoneRequest.getColor());

        Phone savedPhone = (Phone) productRepository.save(phone);
        log.info("Phone {} is saved", savedPhone.getId());
        return mapToPhoneResponse(savedPhone);
    }

    public LaptopResponse createLaptop(LaptopRequest laptopRequest) {
        Laptop laptop = new Laptop();
        // Thiết lập các trường cơ bản của Product
        laptop.setName(laptopRequest.getName());
        laptop.setDescription(laptopRequest.getDescription());
        laptop.setOriginalPrice(laptopRequest.getOriginalPrice());
        laptop.setCurrentPrice(laptopRequest.getCurrentPrice());
        laptop.setBrand(laptopRequest.getBrand());
        laptop.setImage(laptopRequest.getImage());
        laptop.setStockQuantity(laptopRequest.getStockQuantity());
        laptop.setIsAvailable(true);
        laptop.setWarrantyPeriod(laptopRequest.getWarrantyPeriod());
        laptop.setProductReview(laptopRequest.getProductReview());
        laptop.setPromotion(laptopRequest.getPromotion());
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

        Laptop savedLaptop = (Laptop) productRepository.save(laptop);
        log.info("Laptop {} is saved", savedLaptop.getId());
        return mapToLaptopResponse(savedLaptop);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
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

    public ProductResponse getProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        if (product instanceof Phone) {
            return mapToPhoneResponse((Phone) product);
        } else if (product instanceof Laptop) {
            return mapToLaptopResponse((Laptop) product);
        }

        return mapToProductResponse(product);
    }

    public List<ProductResponse> getProductsByCategory(String category) {
        List<Product> products = productRepository.findByBrand(category);
        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> searchProducts(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
        log.info("Product {} is deleted", id);
    }

    private ProductResponse mapToProductResponse(Product product) {
        if (product instanceof Phone) {
            return mapToPhoneResponse((Phone) product);
        } else if (product instanceof Laptop) {
            return mapToLaptopResponse((Laptop) product);
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .originalPrice(product.getOriginalPrice())
                .currentPrice(product.getCurrentPrice())
                .brand(product.getBrand())
                .image(product.getImage())
                .stockQuantity(product.getStockQuantity())
                .isAvailable(product.getIsAvailable())
                .type(product.getType())
                .warrantyPeriod(product.getWarrantyPeriod())
                .productReview(product.getProductReview())
                .promotion(product.getPromotion())
                .release(product.getRelease())
                .build();
    }

    private PhoneResponse mapToPhoneResponse(Phone phone) {
        return PhoneResponse.builder()
                // Map basic product fields
                .id(phone.getId())
                .name(phone.getName())
                .description(phone.getDescription())
                .originalPrice(phone.getOriginalPrice())
                .currentPrice(phone.getCurrentPrice())
                .brand(phone.getBrand())
                .image(phone.getImage())
                .stockQuantity(phone.getStockQuantity())
                .isAvailable(phone.getIsAvailable())
                .type(phone.getType())
                .warrantyPeriod(phone.getWarrantyPeriod())
                .productReview(phone.getProductReview())
                .promotion(phone.getPromotion())
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
                .displayResolutiona(phone.getDisplayResolutiona())
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
                .build();
    }

    private LaptopResponse mapToLaptopResponse(Laptop laptop) {
        return LaptopResponse.builder()
                // Map basic product fields
                .id(laptop.getId())
                .name(laptop.getName())
                .description(laptop.getDescription())
                .originalPrice(laptop.getOriginalPrice())
                .currentPrice(laptop.getCurrentPrice())
                .brand(laptop.getBrand())
                .image(laptop.getImage())
                .stockQuantity(laptop.getStockQuantity())
                .isAvailable(laptop.getIsAvailable())
                .type(laptop.getType())
                .warrantyPeriod(laptop.getWarrantyPeriod())
                .productReview(laptop.getProductReview())
                .promotion(laptop.getPromotion())
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
        phone.setOriginalPrice(phoneRequest.getOriginalPrice());
        phone.setCurrentPrice(phoneRequest.getCurrentPrice());
        phone.setBrand(phoneRequest.getBrand());
        phone.setImage(phoneRequest.getImage());
        phone.setStockQuantity(phoneRequest.getStockQuantity());
        phone.setIsAvailable(true);
        phone.setWarrantyPeriod(phoneRequest.getWarrantyPeriod());
        phone.setProductReview(phoneRequest.getProductReview());
        phone.setPromotion(phoneRequest.getPromotion());
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
        phone.setDisplayResolutiona(phoneRequest.getDisplayResolutiona());
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

        Phone updatedPhone = phoneRepository.save(phone);
        log.info("Phone {} is updated", updatedPhone.getId());
        return mapToPhoneResponse(updatedPhone);
    }

    public LaptopResponse updateLaptop(String id, LaptopRequest laptopRequest) {
        Laptop laptop = laptopRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Laptop not found with id: " + id));

        laptop.setName(laptopRequest.getName());
        laptop.setDescription(laptopRequest.getDescription());
        laptop.setOriginalPrice(laptopRequest.getOriginalPrice());
        laptop.setCurrentPrice(laptopRequest.getCurrentPrice());
        laptop.setBrand(laptopRequest.getBrand());
        laptop.setImage(laptopRequest.getImage());
        laptop.setStockQuantity(laptopRequest.getStockQuantity());
        laptop.setIsAvailable(true);
        laptop.setWarrantyPeriod(laptopRequest.getWarrantyPeriod());
        laptop.setProductReview(laptopRequest.getProductReview());
        laptop.setPromotion(laptopRequest.getPromotion());
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