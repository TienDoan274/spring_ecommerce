package com.eazybytes.service;

import com.eazybytes.client.InventoryClient;
import com.eazybytes.dto.*;
import com.eazybytes.model.*;
import com.eazybytes.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final InventoryClient inventoryClient;

    // Product type constants
    private static final String PHONE_TYPE = "PHONE";
    private static final String LAPTOP_TYPE = "LAPTOP";
    private static final String BACKUP_CHARGER_TYPE = "BACKUP_CHARGER";
    private static final String CABLE_CHARGER_HUB_TYPE = "CABLE_CHARGER_HUB";

    public ProductResponse getProductById(String type, String id) {
        BaseProduct product = findProductById(id);
        List<InventoryDto> inventoryDtos = inventoryClient.getProductColorVariants(id).getBody();

        return mapToProductResponse(type, product, inventoryDtos);
    }

    private BaseProduct findProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found with id: " + id));
    }



    private String determineProductType(ProductRequest request) {
        if (request instanceof PhoneRequest) return PHONE_TYPE;
        if (request instanceof LaptopRequest) return LAPTOP_TYPE;
        if (request instanceof BackupChargerRequest) return BACKUP_CHARGER_TYPE;
        if (request instanceof CableChargerHubRequest) return CABLE_CHARGER_HUB_TYPE;
        throw new IllegalArgumentException("Unknown product request type");
    }

    public ProductResponse createProduct(ProductWithInventoryRequest request) {
        ProductRequest productRequest = request.getProductRequest();
        List<InventoryDto> inventoryRequests = request.getInventoryRequests();

        BaseProduct product;
        // Sử dụng kiểu của ProductRequest để xác định loại sản phẩm
        if (productRequest instanceof PhoneRequest) {
            product = createPhoneFromRequest((PhoneRequest) productRequest);
        } else if (productRequest instanceof LaptopRequest) {
            product = createLaptopFromRequest((LaptopRequest) productRequest);
        } else if (productRequest instanceof BackupChargerRequest) {
            product = createBackupChargerFromRequest((BackupChargerRequest) productRequest);
        } else if (productRequest instanceof CableChargerHubRequest) {
            product = createCableChargerHubFromRequest((CableChargerHubRequest) productRequest);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid product type");
        }

        BaseProduct savedProduct = productRepository.save(product);
        List<InventoryDto> inventoryDtos = createInventories(savedProduct, inventoryRequests);

        // Sử dụng kiểu thực tế để xác định loại sản phẩm trong response
        String productType = determineProductType(productRequest);
        return mapToProductResponse(productType, savedProduct, inventoryDtos);
    }

    private void setBaseProductFields(BaseProduct product, ProductRequest request) {
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setImages(request.getImages());
        product.setWarrantyPeriod(request.getWarrantyPeriod());
        product.setProductReviews(request.getProductReviews());
        product.setPromotions(request.getPromotions());
        product.setRelease(request.getRelease());
        product.setType(request.getType());
    }


    private List<InventoryDto> createInventories(BaseProduct product, List<InventoryDto> requests) {
        List<InventoryDto> inventoryDtos = new ArrayList<>();

        for (InventoryDto request : requests) {
            InventoryDto dto = new InventoryDto();
            dto.setProductName(product.getProductName());
            dto.setProductId(product.getProductId());
            dto.setCurrentPrice(request.getCurrentPrice());
            dto.setOriginalPrice(request.getOriginalPrice());
            dto.setQuantity(request.getQuantity());
            dto.setColor(request.getColor());
            inventoryDtos.add(dto);
            inventoryClient.createInventory(dto);
        }

        return inventoryDtos;
    }

    public ProductResponse updateProduct(String id, ProductRequest request, List<InventoryDto> inventoryDtos) {
        BaseProduct product = findProductById(id);
        String productType = product.getType();

        // Update product fields based on type
        switch (productType) {
            case PHONE_TYPE:
                updatePhoneFields((Phone) product, (PhoneRequest) request);
                break;
            case LAPTOP_TYPE:
                updateLaptopFields((Laptop) product, (LaptopRequest) request);
                break;
            case BACKUP_CHARGER_TYPE:
                updateBackupChargerFields((BackupCharger) product, (BackupChargerRequest) request);
                break;
            case CABLE_CHARGER_HUB_TYPE:
                updateCableChargerHubFields((CableChargerHub) product, (CableChargerHubRequest) request);
                break;
            default:
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid product type: " + productType);
        }

        BaseProduct updatedProduct = productRepository.save(product);

        // Update inventories if needed
        for (InventoryDto dto : inventoryDtos) {
            inventoryClient.updateInventory(dto.getInventoryId(), dto);
        }

        return mapToProductResponse(productType, updatedProduct, inventoryDtos);
    }



    private void updateBaseProductFields(BaseProduct product, ProductRequest request) {
        if (request.getProductName() != null) product.setProductName(request.getProductName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getBrand() != null) product.setBrand(request.getBrand());
        if (request.getImages() != null) product.setImages(request.getImages());
        if (request.getWarrantyPeriod() != null) product.setWarrantyPeriod(request.getWarrantyPeriod());
        if (request.getProductReviews() != null) product.setProductReviews(request.getProductReviews());
        if (request.getPromotions() != null) product.setPromotions(request.getPromotions());
        if (request.getRelease() != null) product.setRelease(request.getRelease());
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    private ProductResponse mapToProductResponse(String type, BaseProduct product, List<InventoryDto> inventoryDtos) {
        switch (type) {
            case PHONE_TYPE:
                return PhoneResponse.fromPhone((Phone) product, inventoryDtos);
            case LAPTOP_TYPE:
                return LaptopResponse.fromLaptop((Laptop) product, inventoryDtos);
            case BACKUP_CHARGER_TYPE:
                return BackupChargerResponse.fromBackupCharger((BackupCharger) product, inventoryDtos);
            case CABLE_CHARGER_HUB_TYPE:
                return CableChargerHubResponse.fromCableChargerHub((CableChargerHub) product, inventoryDtos);
            default:
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid product type: " + type);
        }
    }

    // Phone methods
    private Phone createPhoneFromRequest(PhoneRequest request) {
        Phone phone = new Phone();

        // Thiết lập các trường cơ bản
        setBaseProductFields(phone, request);

        // Thiết lập các trường riêng của Phone
        phone.setOs(request.getOs());
        phone.setProcessor(request.getProcessor());
        phone.setCpuSpeed(request.getCpuSpeed());
        phone.setGpu(request.getGpu());
        phone.setRam(request.getRam());
        phone.setStorage(request.getStorage());
        phone.setAvailableStorage(request.getAvailableStorage());
        phone.setContactLimit(request.getContactLimit());

        // Camera và màn hình
        phone.setRearCameraResolution(request.getRearCameraResolution());
        phone.setRearVideoRecording(request.getRearVideoRecording());
        phone.setRearFlash(request.getRearFlash());
        phone.setRearCameraFeatures(request.getRearCameraFeatures());
        phone.setFrontCameraResolution(request.getFrontCameraResolution());
        phone.setFrontCameraFeatures(request.getFrontCameraFeatures());
        phone.setDisplayTechnology(request.getDisplayTechnology());
        phone.setDisplayResolution(request.getDisplayResolution());
        phone.setScreenSize(request.getScreenSize());
        phone.setMaxBrightness(request.getMaxBrightness());
        phone.setScreenProtection(request.getScreenProtection());

        // Pin và sạc
        phone.setBatteryCapactity(request.getBatteryCapactity());
        phone.setBatteryType(request.getBatteryType());
        phone.setMaxChargingPower(request.getMaxChargingPower());
        phone.setBatteryFeatures(request.getBatteryFeatures());

        // Tiện ích
        phone.setSecurityFeatures(request.getSecurityFeatures());
        phone.setSpecialFeatures(request.getSpecialFeatures());
        phone.setWaterResistance(request.getWaterResistance());
        phone.setRecording(request.getRecording());
        phone.setVideo(request.getVideo());
        phone.setAudio(request.getAudio());

        // Kết nối
        phone.setMobileNetwork(request.getMobileNetwork());
        phone.setSimType(request.getSimType());
        phone.setWifi(request.getWifi());
        phone.setGps(request.getGps());
        phone.setBluetooth(request.getBluetooth());
        phone.setChargingPort(request.getChargingPort());
        phone.setHeadphoneJack(request.getHeadphoneJack());
        phone.setOtherConnectivity(request.getOtherConnectivity());

        // Thiết kế
        phone.setDesignType(request.getDesignType());
        phone.setMaterials(request.getMaterials());
        phone.setSizeWeight(request.getSizeWeight());

        return phone;
    }

    private void updatePhoneFields(Phone phone, PhoneRequest request) {
        // Update base fields
        updateBaseProductFields(phone, request);

        phone.setOs(request.getOs());
        phone.setProcessor(request.getProcessor());
        phone.setCpuSpeed(request.getCpuSpeed());
        phone.setGpu(request.getGpu());
        phone.setRam(request.getRam());
        phone.setStorage(request.getStorage());
        phone.setAvailableStorage(request.getAvailableStorage());
        phone.setContactLimit(request.getContactLimit());

        // Camera và màn hình
        phone.setRearCameraResolution(request.getRearCameraResolution());
        phone.setRearVideoRecording(request.getRearVideoRecording());
        phone.setRearFlash(request.getRearFlash());
        phone.setRearCameraFeatures(request.getRearCameraFeatures());
        phone.setFrontCameraResolution(request.getFrontCameraResolution());
        phone.setFrontCameraFeatures(request.getFrontCameraFeatures());
        phone.setDisplayTechnology(request.getDisplayTechnology());
        phone.setDisplayResolution(request.getDisplayResolution());
        phone.setScreenSize(request.getScreenSize());
        phone.setMaxBrightness(request.getMaxBrightness());
        phone.setScreenProtection(request.getScreenProtection());

        // Pin và sạc
        phone.setBatteryCapactity(request.getBatteryCapactity());
        phone.setBatteryType(request.getBatteryType());
        phone.setMaxChargingPower(request.getMaxChargingPower());
        phone.setBatteryFeatures(request.getBatteryFeatures());

        // Tiện ích
        phone.setSecurityFeatures(request.getSecurityFeatures());
        phone.setSpecialFeatures(request.getSpecialFeatures());
        phone.setWaterResistance(request.getWaterResistance());
        phone.setRecording(request.getRecording());
        phone.setVideo(request.getVideo());
        phone.setAudio(request.getAudio());

        // Kết nối
        phone.setMobileNetwork(request.getMobileNetwork());
        phone.setSimType(request.getSimType());
        phone.setWifi(request.getWifi());
        phone.setGps(request.getGps());
        phone.setBluetooth(request.getBluetooth());
        phone.setChargingPort(request.getChargingPort());
        phone.setHeadphoneJack(request.getHeadphoneJack());
        phone.setOtherConnectivity(request.getOtherConnectivity());

        // Thiết kế
        phone.setDesignType(request.getDesignType());
        phone.setMaterials(request.getMaterials());
        phone.setSizeWeight(request.getSizeWeight());


    }

    // Laptop methods
    private Laptop createLaptopFromRequest(LaptopRequest request) {
        Laptop laptop = new Laptop();

        // Thiết lập các trường cơ bản
        setBaseProductFields(laptop, request);

        // Thiết lập các trường riêng của Laptop
        laptop.setProcessorModel(request.getProcessorModel());
        laptop.setCoreCount(request.getCoreCount());
        laptop.setThreadCount(request.getThreadCount());
        laptop.setCpuSpeed(request.getCpuSpeed());
        laptop.setMaxCpuSpeed(request.getMaxCpuSpeed());

        // RAM, ổ cứng
        laptop.setRam(request.getRam());
        laptop.setRamType(request.getRamType());
        laptop.setRamBusSpeed(request.getRamBusSpeed());
        laptop.setMaxRam(request.getMaxRam());
        laptop.setStorage(request.getStorage());

        // Màn hình
        laptop.setScreenSize(request.getScreenSize());
        laptop.setResolution(request.getResolution());
        laptop.setRefreshRate(request.getRefreshRate());
        laptop.setColorGamut(request.getColorGamut());
        laptop.setDisplayTechnology(request.getDisplayTechnology());

        // Đồ họa và âm thanh
        laptop.setGraphicCard(request.getGraphicCard());
        laptop.setAudioTechnology(request.getAudioTechnology());
        laptop.setPorts(request.getPorts());
        laptop.setWirelessConnectivity(request.getWirelessConnectivity());
        laptop.setWebcam(request.getWebcam());
        laptop.setOtherFeatures(request.getOtherFeatures());
        laptop.setKeyboardBacklight(request.getKeyboardBacklight());

        // Kích thước - khối lượng - pin
        laptop.setSize(request.getSize());
        laptop.setMaterial(request.getMaterial());
        laptop.setBattery(request.getBattery());
        laptop.setOs(request.getOs());

        return laptop;
    }

    private void updateLaptopFields(Laptop laptop, LaptopRequest request) {
        // Update base fields
        updateBaseProductFields(laptop, request);

        // Update Laptop specific fields
        laptop.setProcessorModel(request.getProcessorModel());
        laptop.setCoreCount(request.getCoreCount());
        laptop.setThreadCount(request.getThreadCount());
        laptop.setCpuSpeed(request.getCpuSpeed());
        laptop.setMaxCpuSpeed(request.getMaxCpuSpeed());

        // RAM, ổ cứng
        laptop.setRam(request.getRam());
        laptop.setRamType(request.getRamType());
        laptop.setRamBusSpeed(request.getRamBusSpeed());
        laptop.setMaxRam(request.getMaxRam());
        laptop.setStorage(request.getStorage());

        // Màn hình
        laptop.setScreenSize(request.getScreenSize());
        laptop.setResolution(request.getResolution());
        laptop.setRefreshRate(request.getRefreshRate());
        laptop.setColorGamut(request.getColorGamut());
        laptop.setDisplayTechnology(request.getDisplayTechnology());

        // Đồ họa và âm thanh
        laptop.setGraphicCard(request.getGraphicCard());
        laptop.setAudioTechnology(request.getAudioTechnology());
        laptop.setPorts(request.getPorts());
        laptop.setWirelessConnectivity(request.getWirelessConnectivity());
        laptop.setWebcam(request.getWebcam());
        laptop.setOtherFeatures(request.getOtherFeatures());
        laptop.setKeyboardBacklight(request.getKeyboardBacklight());

        // Kích thước - khối lượng - pin
        laptop.setSize(request.getSize());
        laptop.setMaterial(request.getMaterial());
        laptop.setBattery(request.getBattery());
        laptop.setOs(request.getOs());
    }

    // BackupCharger methods
    private BackupCharger createBackupChargerFromRequest(BackupChargerRequest request) {
        BackupCharger backupCharger = new BackupCharger();

        // Thiết lập các trường cơ bản
        setBaseProductFields(backupCharger, request);

        // Thiết lập các trường riêng của BackupCharger
        backupCharger.setBatteryCapactity(request.getBatteryCapactity());
        backupCharger.setChargingTime(request.getChargingTime());
        backupCharger.setBatteryCellType(request.getBatteryCellType());
        backupCharger.setTechnologyFeatures(request.getTechnologyFeatures());
        backupCharger.setChargingTime(request.getChargingTime());
        backupCharger.setOutput(request.getOutput());
        backupCharger.setInput(request.getInput());
        backupCharger.setSize(request.getSize());
        backupCharger.setWeight(request.getWeight());
        backupCharger.setBrandOrigin(request.getBrandOrigin());
        backupCharger.setManufactured(request.getManufactured());

        return backupCharger;
    }

    private void updateBackupChargerFields(BackupCharger backupCharger, BackupChargerRequest request) {
        // Update base fields
        updateBaseProductFields(backupCharger, request);

        // Update BackupCharger specific fields
        backupCharger.setBatteryCapactity(request.getBatteryCapactity());
        backupCharger.setChargingTime(request.getChargingTime());
        backupCharger.setBatteryCellType(request.getBatteryCellType());
        backupCharger.setTechnologyFeatures(request.getTechnologyFeatures());
        backupCharger.setChargingTime(request.getChargingTime());
        backupCharger.setOutput(request.getOutput());
        backupCharger.setInput(request.getInput());
        backupCharger.setSize(request.getSize());
        backupCharger.setWeight(request.getWeight());
        backupCharger.setBrandOrigin(request.getBrandOrigin());
        backupCharger.setManufactured(request.getManufactured());
    }

    // CableChargerHub methods
    private CableChargerHub createCableChargerHubFromRequest(CableChargerHubRequest request) {
        CableChargerHub cableChargerHub = new CableChargerHub();

        // Thiết lập các trường cơ bản
        setBaseProductFields(cableChargerHub, request);

        // Thiết lập các trường riêng của CableChargerHub
        cableChargerHub.setFeatures(request.getFeatures());
        cableChargerHub.setModel(request.getModel());
        cableChargerHub.setTechnologyFeatures(request.getTechnologyFeatures());
        cableChargerHub.setMaximumCharging(request.getMaximumCharging());
        cableChargerHub.setOutput(request.getOutput());
        cableChargerHub.setInput(request.getInput());
        cableChargerHub.setSize(request.getSize());
        cableChargerHub.setBrandOrigin(request.getBrandOrigin());
        cableChargerHub.setManufactured(request.getManufactured());
        cableChargerHub.setLength(request.getLength());
        cableChargerHub.setConnectionJack(request.getConnectionJack());

        return cableChargerHub;
    }

    private void updateCableChargerHubFields(CableChargerHub cableChargerHub, CableChargerHubRequest request) {
        // Update base fields
        updateBaseProductFields(cableChargerHub, request);

        // Update CableChargerHub specific fields
        cableChargerHub.setFeatures(request.getFeatures());
        cableChargerHub.setModel(request.getModel());
        cableChargerHub.setTechnologyFeatures(request.getTechnologyFeatures());
        cableChargerHub.setMaximumCharging(request.getMaximumCharging());
        cableChargerHub.setOutput(request.getOutput());
        cableChargerHub.setInput(request.getInput());
        cableChargerHub.setSize(request.getSize());
        cableChargerHub.setBrandOrigin(request.getBrandOrigin());
        cableChargerHub.setManufactured(request.getManufactured());
        cableChargerHub.setLength(request.getLength());
        cableChargerHub.setConnectionJack(request.getConnectionJack());
    }


}