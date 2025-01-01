package com.bankapp.messagerouter.config;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.JMSException;
import javax.jms.QueueConnectionFactory;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQQueue;
import com.ibm.msg.client.wmq.WMQConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.SingleConnectionFactory;

@Configuration
public class MqConfig {

    @Value("${ibm.mq.queueManager}")
    private String queueManager;

    @Value("${ibm.mq.channel}")
    private String channel;

    @Value("${ibm.mq.user}")
    private String user;

    @Value("${ibm.mq.password}")
    private String password;



    @Value("${ibm.mq.host}")
    private String host;

    @Value("${ibm.mq.port}")
    private int port;

    @Value("${ibm.mq.transport}")
    private String transport;

    @Value("${ibm.mq.inputQueue}")
    private String inputQueueName;

    @Bean
    public QueueConnectionFactory queueConnectionFactory() throws JMSException {
        MQQueueConnectionFactory connectionFactory = new MQQueueConnectionFactory();
        connectionFactory.setHostName(host);
        connectionFactory.setPort(port);
        connectionFactory.setChannel(channel);
        connectionFactory.setQueueManager(queueManager);
        connectionFactory.setTransportType(transport.equalsIgnoreCase("TCP") ? 1 : 0);  // TCP = 1, SSL = 0
        return connectionFactory;
    }

    @Bean
    public Queue inputQueue() throws JMSException {
        return new MQQueue(inputQueueName);
    }

    @Bean
    public MQQueueConnectionFactory mqQueueConnectionFactory() throws JMSException {
        MQQueueConnectionFactory factory = new MQQueueConnectionFactory();
        factory.setHostName("localhost");
        factory.setPort(1414); // Port
        factory.setQueueManager(queueManager); // Use your queue manager
        factory.setChannel(channel); // Use your channel
        factory.setTransportType(WMQConstants.WMQ_CM_CLIENT); // Use client connection mode
        return factory;
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory(MQQueueConnectionFactory mqQueueConnectionFactory) throws JMSException {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(mqQueueConnectionFactory);
        cachingConnectionFactory.setSessionCacheSize(10); // Configure session cache size
        return cachingConnectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(CachingConnectionFactory cachingConnectionFactory) {
        return new JmsTemplate(cachingConnectionFactory);
    }

    @Bean
    public Queue queue() throws JMSException {
        return new MQQueue("DEV.QUEUE.1"); // Use MQQueue for IBM MQ
    }

//    @Bean
//    public MessageListenerContainer messageListenerContainer(CachingConnectionFactory cachingConnectionFactory) throws JMSException {
//        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
//        container.setConnectionFactory(cachingConnectionFactory);
//        container.setDestination(queue()); // Set the queue name
//        container.setMessageListener(messageListenerAdapter()); // Set the listener
//        return container;
//    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(new MqMessageListener());
    }

    @Bean
    public DefaultMessageListenerContainer messageListenerContainer(CachingConnectionFactory cachingConnectionFactory) throws JMSException {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(cachingConnectionFactory);
        container.setDestination(queue());
        container.setMessageListener(messageListenerAdapter());
        container.setConcurrency("1-5");  // Set concurrency here
        return container;
    }

}
