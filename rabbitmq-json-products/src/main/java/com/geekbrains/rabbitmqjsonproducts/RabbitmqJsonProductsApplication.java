package com.geekbrains.rabbitmqjsonproducts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Queue;
@SpringBootApplication
public class RabbitmqJsonProductsApplication {


    // бины с очередями, как были на уроке
    // сервис фронта стартует аналогично, прикладывать в пул реквест не буду
    public static final boolean NON_DURABLE = false;
    public static final String REQUEST_QUEUE = "requestQueue";
    public static final String RESPONSE_QUEUE = "responseQueue";

    @Bean
    public Queue requestQueue() {
        return new Queue(REQUEST_QUEUE, NON_DURABLE);
    }

    @Bean
    public Queue responseQueue() {
        return new Queue(RESPONSE_QUEUE, NON_DURABLE);
    }


    public static void main(String[] args) {
        SpringApplication.run(RabbitmqJsonProductsApplication.class, args);
    }

}
