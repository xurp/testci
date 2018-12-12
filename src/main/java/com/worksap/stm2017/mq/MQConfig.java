package com.worksap.stm2017.mq;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
	
	public static final String MIAOSHA_QUEUE = "me.queue";
	public static final String QUEUE = "queue";
	public static final String TOPIC_QUEUE1 = "topic.message";	
	public static final String TOPIC_EXCHANGE = "exchange";
	/*public static final String FANOUT_EXCHANGE = "fanoutxchage";
	public static final String HEADERS_EXCHANGE = "headersExchage";*/

	/*最简单的
	@Bean
	public MessageConverter getMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	@Bean
	public Queue queue() {
		return new Queue(QUEUE, true);
	}
	*/
	
	/**
	 * Direct模式 交换机Exchange
	 * */
	/*@Bean
	public Queue queue() {
		return new Queue(QUEUE, true);
	}
	@Bean
	public DirectExchange topicDirect(){
		return new DirectExchange(TOPIC_EXCHANGE);
	}
	
	*//**
	 * Topic模式 交换机Exchange
	 * */
	@Bean
	public Queue topicQueue1() {
		//return new Queue(TOPIC_QUEUE1, true);
		return new Queue(TOPIC_QUEUE1);
	}
	@Bean
	public TopicExchange topicExchage(){
		return new TopicExchange(TOPIC_EXCHANGE);
	}
	//注：这里的两个参数应该要和上面的两个Bean重名
	@Bean
    Binding bindingExchangeMessage(Queue topicQueue1, TopicExchange topicExchage) {
		//队列绑定交换机绑定路由规则
        return BindingBuilder.bind(topicQueue1).to(topicExchage).with("topic.#");
    }
	/*
	@Bean
	public Binding topicBinding1() {
		return BindingBuilder.bind(topicQueue1()).to(topicExchage()).with("topic.key1");
	}
	*/
	
	@Bean
    public Queue AMessage() {
        return new Queue("fanout.A");
    }
	
	@Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }
	//发到fanoutExchange的sender全部转给topicQueue1队列
	@Bean
    Binding bindingExchangeA(Queue AMessage,FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(AMessage).to(fanoutExchange);
    }
	
}
