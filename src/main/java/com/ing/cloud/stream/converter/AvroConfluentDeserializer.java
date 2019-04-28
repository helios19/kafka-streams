package com.ing.cloud.stream.converter;

import org.apache.avro.Schema;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;

public class AvroConfluentDeserializer<T extends SpecificRecordBase> implements Deserializer<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AvroConfluentDeserializer.class);

    protected static final byte MAGIC_BYTE = 0x0;

    protected static final int idSize = 4;

    private final DecoderFactory decoderFactory = DecoderFactory.get();

    protected final Class<T> targetType;

    public AvroConfluentDeserializer(Class<T> targetType) {
        this.targetType = targetType;
    }

    @Override
    public void close() {
        // No-op
    }

    @Override
    public void configure(Map<String, ?> arg0, boolean arg1) {
        // No-op
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            T result = null;
            if (data != null) {
                LOG.info("data='{}'", DatatypeConverter.printHexBinary(data));
                result = (T) deserializePayload(data, targetType.newInstance().getSchema());
                LOG.info("deserialized data='{}'", result);
            }
            return result;
        } catch (Exception ex) {
            throw new SerializationException(
                    "Can't deserialize data '" + Arrays.toString(data) + "' from topic '" + topic + "'", ex);
        }
    }

    protected T deserializePayload(byte[] payload, Schema schema) throws SerializationException {
        int id = -1;
        try {
            ByteBuffer buffer = getByteBuffer(payload);
            id = buffer.getInt();
            int length = buffer.limit() - 1 - idSize;
            int start = buffer.position() + buffer.arrayOffset();
            DatumReader<T> reader = new SpecificDatumReader<T>(schema);
            return reader.read(null, decoderFactory.binaryDecoder(buffer.array(), start, length, null));
        } catch (IOException | RuntimeException e) {
            throw new SerializationException("Error deserializing Avro message for id " + id, e);
        }
    }

    private ByteBuffer getByteBuffer(byte[] payload) {
        ByteBuffer buffer = ByteBuffer.wrap(payload);
        if (buffer.get() != MAGIC_BYTE) {
            throw new SerializationException("Unknown magic byte!");
        }
        return buffer;
    }
}