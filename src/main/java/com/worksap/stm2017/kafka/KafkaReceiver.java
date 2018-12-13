package com.worksap.stm2017.kafka;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class KafkaReceiver {
    private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());
    
    //可以监听多个
    @KafkaListener(topics = {"zhisheng","string"})
    public void listen(ConsumerRecord<?, ?> record) {
    	//Returns an Optional describing the specified value, if non-null,otherwise returns an empty Optional.
    	//record.value() 是另一种取object的方式（书上）
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {

            Object message = kafkaMessage.get();

            log.info("----------------- record =" + record);
            log.info("------------------ message =" + message);
        }

    }
    
}