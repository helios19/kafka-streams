package com.ing.cloud.stream.config;

import com.ing.cloud.stream.converter.AvroSchemaMessageConverter;
import com.ing.cloud.stream.processor.KafkaStreamProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamMessageConverter;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MessageConverter;

@Slf4j
@EnableBinding(KafkaStreamProcessor.class)
@EnableSchemaRegistryClient
//@EnableBinding(KafkaStreamsProcessor.class)
public class StreamsConfig {

    @Bean
    @StreamMessageConverter
    public MessageConverter userMessageConverter() {
//        AvroSchemaMessageConverter converter = new AvroSchemaMessageConverter(MimeType.valueOf("avro/bytes"));
        AvroSchemaMessageConverter converter = new AvroSchemaMessageConverter();
//        converter.setSchemaLocation(new ClassPathResource("avro/mysqlcdc.test.RawTransaction.avsc"));
        return converter;
    }

}
