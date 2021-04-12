package com.example.rabbitmqproductsfrondtend;

import com.example.rabbitmqproductsfrondtend.dto.ProductDto;
import com.example.rabbitmqproductsfrondtend.dto.ResponseDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.rabbitmqproductsfrondtend.RabbitmqProductsFrondtendApplication.RESPONSE_QUEUE;
import static com.example.rabbitmqproductsfrondtend.RabbitmqProductsFrondtendApplication.REQUEST_QUEUE;

// контроллер для фронта
@Controller
public class ProductsController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // кэш с ответами
    Map<String, ResponseDto> responsesCache = new HashMap<>();

    // страница с таблицей товаров, получаем список товаров
    @GetMapping("/products")
    public String greeting(Model model) throws InterruptedException {

        // сначала сгенерируем uuid для запроса в сервис бэка
        String uuid = UUID.randomUUID().toString();
        ResponseDto responseDto = new ResponseDto(); // создаем новый объект для запроса
        responseDto.setUuid(uuid); // присваем uuid
        responseDto.setType("get-products"); // и тип

        rabbitTemplate.convertAndSend(REQUEST_QUEUE, responseDto); // шлём к бэку

        // потом в цикле каджые 100мс посылаем повторные запросы,
        // пока в кэш responsesCache не прилетит ответ с нужным нам uuid
        do {
            Thread.sleep(100);
            rabbitTemplate.convertAndSend(REQUEST_QUEUE, responseDto);
        } while (!responsesCache.containsKey(uuid));

        // устаналиваем коллекцию товаров в модель
        model.addAttribute("products", responsesCache.get(uuid).getProductDtos());
        responsesCache.remove(uuid); // удаляем из кэша ответов обработанный ответ
        ProductDto productDto = new ProductDto(); // это для формочки добавления нового товара
        model.addAttribute("product", productDto);

        return "products-page";
    }

    // добавление нового товара
    // всё аналогично предыдущему методу
    @PostMapping("add-product")
    public String addProduct(ProductDto productDto) throws InterruptedException {

        String uuid = UUID.randomUUID().toString();
        ResponseDto responseDto = new ResponseDto();
        responseDto.setUuid(uuid);
        responseDto.setType("add-product");
        responseDto.setAddProduct(productDto);

        rabbitTemplate.convertAndSend(REQUEST_QUEUE, responseDto);

        do {
            Thread.sleep(100);
            rabbitTemplate.convertAndSend(REQUEST_QUEUE, responseDto);
        } while (!responsesCache.containsKey(uuid));

        responsesCache.remove(uuid);


        return "redirect:/products";
    }

    // удаление происходит аналогично
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) throws InterruptedException {

        String uuid = UUID.randomUUID().toString();
        ResponseDto responseDto = new ResponseDto();
        responseDto.setUuid(uuid);
        responseDto.setType("delete-product");
        responseDto.setDeleteId(id);

        rabbitTemplate.convertAndSend(REQUEST_QUEUE, responseDto);

        do {
            Thread.sleep(100);
            rabbitTemplate.convertAndSend(REQUEST_QUEUE, responseDto);
        } while (!responsesCache.containsKey(uuid));

        responsesCache.remove(uuid);

        return "redirect:/products";
    }

    // листенер, который ждёт ответы от сервиса бэка и при получении записывает их в кэщ responsesCache
    // собственно этот кеш и проверяют методы выше
    @RabbitListener(queues = RESPONSE_QUEUE)
    public void listen(ResponseDto in) {
        responsesCache.put(in.getUuid(), in);
    }

}
