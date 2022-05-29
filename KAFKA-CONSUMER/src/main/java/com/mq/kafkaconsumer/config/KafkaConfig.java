package com.mq.kafkaconsumer.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${kafka.host}")
    private String host;

    @Value("${kafka.username}")
    String username;

    @Value("${kafka.password}")
    String password;

    public final static String TOPIC = "kafka-topic-kmq";

    public final static String GROUP_ID = "kafka-group-id-gp";

    private Map<String, Object> consumerConfigs() {

        JsonDeserializer<GenericOrderResponseMapper>
                deserializer = getDeserializer();

        final Map<String, Object> hashMap = new HashMap<>();
//        hashMap.put("security.protocol", "SASL_SSL");
//
//        hashMap.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
//        hashMap.put(SaslConfigs.SASL_JAAS_CONFIG,
//                    String.format("org.apache.kafka.common.security.plain.PlainLoginModule required " +
//                                          "      username=\"%s\" " +
//                                          "      password=\"%s\";", username, password));
        hashMap.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        hashMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        hashMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        hashMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        hashMap.put(JsonDeserializer.TRUSTED_PACKAGES, "com.mq.kafkaconsumer");
        return hashMap;
    }

    private JsonDeserializer<GenericOrderResponseMapper> getDeserializer() {
        JsonDeserializer<GenericOrderResponseMapper> deserializer = new JsonDeserializer<>(GenericOrderResponseMapper.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);
        return deserializer;
    }

    @Bean
    public ConsumerFactory<String, GenericOrderResponseMapper> consumerFactory() {

        return new DefaultKafkaConsumerFactory<>(consumerConfigs(),
                                                 new StringDeserializer(),
                                                 getDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GenericOrderResponseMapper>
    getKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, GenericOrderResponseMapper> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

}
