package com.eazybytes.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneStorageVariant {
    private String storage;      // Ví dụ: "64 GB"
    private String availableStorage;     // Ví dụ: "58.2 GB"
}