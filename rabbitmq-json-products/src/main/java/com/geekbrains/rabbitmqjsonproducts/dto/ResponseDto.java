package com.geekbrains.rabbitmqjsonproducts.dto;

import java.util.List;

// DTO сообщения для обмена между сервисами
// на стороне сервиса фронта аналогичный DTO, его прикладывать не буду в пул реквест
public class ResponseDto {

    String uuid; // уникальный номер
    String type; // тип сообщения
    List<ProductDto> productDtos; // поле под саисок продуктов
    Long deleteId; // после под id удаляемого продукта
    ProductDto addProduct; // поле под создание продукта


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<ProductDto> getProductDtos() {
        return productDtos;
    }

    public void setProductDtos(List<ProductDto> productDtos) {
        this.productDtos = productDtos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(Long deleteId) {
        this.deleteId = deleteId;
    }

    public ProductDto getAddProduct() {
        return addProduct;
    }

    public void setAddProduct(ProductDto addProduct) {
        this.addProduct = addProduct;
    }
}
