package com.worksap.stm2017.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MQSender {

	private static Logger log = LoggerFactory.getLogger(MQSender.class);
	
	@Autowired
	AmqpTemplate amqpTemplate ;
	
	public void sendSeckillMessage(SeckillMessage mm) {
		log.info("send message mm:"+mm.getId()+" "+mm.getName());
		//amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, mm);
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.rp", mm);
	}
	
	public void send() {
		String context = "hi, fanout msg ";
		System.out.println("Sender : " + context);
		amqpTemplate.convertAndSend("fanoutExchange","", context);
    }

}
