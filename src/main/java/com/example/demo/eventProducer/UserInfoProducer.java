package com.example.demo.eventProducer;

import com.example.demo.Serializer.UserInfoSerializer;
import com.example.demo.entities.UserInfo;
import com.example.demo.model.UserInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;  // ✅ Correct for messaging

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
@Service
public class UserInfoProducer {

    private final KafkaTemplate<String, UserInfoDto> kafkaTemplate;

    private static final Logger log = LoggerFactory.getLogger(UserInfoProducer.class);


    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;

    @Autowired
    public UserInfoProducer(KafkaTemplate<String, UserInfoDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEventToKafka(UserInfoDto eventData) {
        // Convert DTO to JSON
        Message<UserInfoDto> message = MessageBuilder.withPayload(eventData)
                .setHeader(KafkaHeaders.TOPIC, TOPIC_NAME)
                .build();
        log.info("Sending message to topic {}", TOPIC_NAME);
        kafkaTemplate.send(message);
    }
}

