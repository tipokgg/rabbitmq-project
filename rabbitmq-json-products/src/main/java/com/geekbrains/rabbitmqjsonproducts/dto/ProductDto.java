package com.geekbrains.rabbitmqjsonproducts.dto;

// DTO  продукта для обмена между сервисами
// на стороне сервиса фронта аналогичный DTO, его прикладывать не буду в пул реквест
public class ProductDto {

    private Long id;
    private String vendorCode;
    private String title;
    private String shortDescription;
    private double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}