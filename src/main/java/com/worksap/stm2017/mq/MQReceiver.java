package com.worksap.stm2017.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

		private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
		
		//@RabbitListener(queues=MQConfig.MIAOSHA_QUEUE)
		//@RabbitHandler
		@RabbitListener(queues = MQConfig.TOPIC_QUEUE1) //队列TOPIC_QUEUE1绑定了路由规则topic.#，所以这里要监听TOPIC_QUEUE1
		//而sender里的topic.rp是用来匹配路由规则的
		public void receive(SeckillMessage mm) {
			//前面想把发string的也发到TOPIC_QUEUE1，但这里也会收到，会出现转换错误
			log.info("receive message:"+mm.getName());			
			
		}
		
		@RabbitListener(queues = "fanout.A") 
		public void receive1(String message) {
			log.info("receive message:"+message);			
			
		}
}
