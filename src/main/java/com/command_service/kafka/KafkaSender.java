package com.command_service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    String kafkaTopic = "kafka_test_topic";

    public void send(String message) {
        kafkaTemplate.send(kafkaTopic, message);
    }

    public void sendMessage(String message) throws ExecutionException, InterruptedException {
        CompletableFuture<SendResult<String, String>> future = (CompletableFuture<SendResult<String, String>>) kafkaTemplate.send(kafkaTopic, message);
        future.get();
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        message + "] due to : " + ex.getMessage());
            }
        });
    }

}
