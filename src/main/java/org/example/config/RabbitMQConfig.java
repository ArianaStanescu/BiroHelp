package org.example.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CLIENTS_REQUESTS_QUEUE = "clients_requests_queue";

    @Bean
    public Queue commandQueue() {
        return new Queue(CLIENTS_REQUESTS_QUEUE);
    }
}
