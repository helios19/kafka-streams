package com.ing.cloud.stream.converter;

import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.ing.cloud.stream.config.SchemaRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.schema.avro.AbstractAvroMessageConverter;
import org.springframework.core.io.Resource;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.kafka.support.JacksonPresent;
import org.springframework.kafka.support.KafkaHeaderMapper;
import org.springframework.kafka.support.SimpleKafkaHeaderMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;

import java.io.IOException;
import java.util.Collection;

@Slf4j
public class AvroSchemaMessageConverter extends AbstractAvroMessageConverter {
    private Schema schema;

    private AvroMapper avroMapper;
    private SchemaRegistry schemaRegistry;
    private KafkaHeaderMapper headerMapper;


    public AvroSchemaMessageConverter() {
        super(MimeType.valueOf("application/*+avro"));
    }

    public AvroSchemaMessageConverter(AvroMapper avroMapper, SchemaRegistry schemaRegistry) {
        this();
        this.avroMapper = avroMapper;
        this.schemaRegistry = schemaRegistry;
        if (JacksonPresent.isJackson2Present()) {
            this.headerMapper = new DefaultKafkaHeaderMapper();
        } else {
            this.headerMapper = new SimpleKafkaHeaderMapper();
        }
    }

    public AvroSchemaMessageConverter(MimeType supportedMimeType) {
        super(supportedMimeType);
    }

    public AvroSchemaMessageConverter(Collection<MimeType> supportedMimeTypes) {
        super(supportedMimeTypes);
    }

    public Schema getSchema() {
        return this.schema;
    }

    public void setSchema(Schema schema) {
        Assert.notNull(schema, "schema cannot be null");
        this.schema = schema;
    }

    public void setSchemaLocation(Resource schemaLocation) {
        Assert.notNull(schemaLocation, "schema cannot be null");

        try {
            this.schema = this.parseSchema(schemaLocation);
        } catch (IOException var3) {
            throw new IllegalStateException("Schema cannot be parsed:", var3);
        }
    }

    @Override
    protected Object convertFromInternal(Message<?> message, Class<?> targetClass, Object conversionHint) {
        System.out.println("in AvroSchemaMessageConverter.convertFromInternal method");

        Object result = null;

        try {
            // deserialize using avro confluent class
            byte[] e = (byte[]) ((byte[]) message.getPayload());

            AvroConfluentDeserializer deserializer = new AvroConfluentDeserializer(targetClass);//Envelope.class);

            result = deserializer.deserialize(null, e);

            log.info("----------------------------------------");
            log.info("result.toString() : " + result.toString());
            log.info("result.getClass() : " + result.getClass().getName());
            log.info("----------------------------------------");


            // convert to targetclass
            ModelMapper modelMapper = new ModelMapper();

            result = modelMapper.map(result, targetClass);


//            result = reader.read((Object)null, decoder);
            return result;
        } catch (Exception var11) {
            log.error("Exception in AvroMessageConverter", var11);
            throw new MessageConversionException(message, "Failed to read payload", var11);
        }


//        return super.convertFromInternal(message, targetClass, conversionHint);
    }

    protected boolean supports(Class<?> clazz) {
        return true;
    }

    protected Schema resolveWriterSchemaForDeserialization(MimeType mimeType) {
        return this.schema;
    }

    protected Schema resolveReaderSchemaForDeserialization(Class<?> targetClass) {
        return this.schema;
    }

    protected Schema resolveSchemaForWriting(Object payload, MessageHeaders headers, MimeType hintedContentType) {
        return this.schema;
    }
}
