package com.geekbrains.rabbitmqjsonproducts.rabbitmq;

import com.geekbrains.rabbitmqjsonproducts.dto.ProductDto;
import com.geekbrains.rabbitmqjsonproducts.dto.ResponseDto;
import com.geekbrains.rabbitmqjsonproducts.entity.Category;
import com.geekbrains.rabbitmqjsonproducts.entity.Product;
import com.geekbrains.rabbitmqjsonproducts.service.CategoryService;
import com.geekbrains.rabbitmqjsonproducts.service.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.geekbrains.rabbitmqjsonproducts.RabbitmqJsonProductsApplication.RESPONSE_QUEUE;
import static com.geekbrains.rabbitmqjsonproducts.RabbitmqJsonProductsApplication.REQUEST_QUEUE;

// сервис по взамодействию с RabbitMQ
@Service
public class RabbitMQService {


    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // мапа для хранения готовых ответов, ключ - uuid, который мы получаем при запросе от сервиса фронта
    Map<String, ResponseDto> cache = new HashMap<>();

    // листенер на очередь запросов
    @RabbitListener(queues = REQUEST_QUEUE)
    public void getProductsListen(ResponseDto in) {

        // сначала проверяем по uuid, есть ли уже ответ в кеше
        if (!cache.containsKey(in.getUuid())) {
            // если нет, то готовим ответ в зависимости от типа запроса и помещаем его в кеш,
            // чтобы потом можно было отправить на фронт
            if (in.getType().equals("get-products")) {
                ResponseDto responseDto = new ResponseDto();
                responseDto.setUuid(in.getUuid()); // заполняем uuid ответа, чтобы фронт знал на какой запрос ответ
                responseDto.setProductDtos(getProducts()); // кладем в ответ список товаров
                cache.put(in.getUuid(), responseDto);
            } else if (in.getType().equals("delete-product")) {
                ResponseDto responseDto = new ResponseDto();
                responseDto.setUuid(in.getUuid());
                deleteProduct(in.getDeleteId()); // удаляем по id
                cache.put(in.getUuid(), responseDto);
            } else if (in.getType().equals("add-product")) {
                ResponseDto responseDto = new ResponseDto();
                responseDto.setUuid(in.getUuid());
                addProduct(in.getAddProduct()); // добавляем новый товар
                cache.put(in.getUuid(), responseDto);
            }
        } else {
            // если в кеше уже есть ответ с таким uuid то шлем его на сервис фронта
            rabbitTemplate.convertAndSend(RESPONSE_QUEUE, cache.get(in.getUuid()));
        }
    }


    // метод добавления  товара
    public List<ProductDto> getProducts() {

        // получаем коллекцию товаров из базы
        List<Product> products = productService.getAllProducts();
        List<ProductDto> result = new ArrayList<>();

        // готовим коллекцию с ДТОшками
        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setVendorCode(product.getVendorCode());
            productDto.setPrice(product.getPrice());
            productDto.setTitle(product.getTitle());
            productDto.setShortDescription(product.getShortDescription());
            result.add(productDto);
        }

        return result;
    }

    // удаляем товар
    public void deleteProduct(Long id) {
        productService.deleteById(id);
    }

    // добавляем в базу новый товар
    public void addProduct(ProductDto productDto) {

        Category category = categoryService.getCategoryById(1L); // ставим первую категорию (хардкод, чтобы не прикручивать категории к фронту)

        // заполняем поля сущности
        Product product = new Product();
        product.setCategory(category);
        product.setVendorCode(productDto.getVendorCode());
        product.setPrice(productDto.getPrice());
        product.setTitle(productDto.getTitle());
        product.setShortDescription(productDto.getShortDescription());
        product.setFullDescription("-");

        // сохраняем в базу
        productService.saveProduct(product);

    }
}
