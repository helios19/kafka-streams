package com.ing.cloud.stream.processor;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import reactor.core.publisher.Flux;

public interface KafkaStreamProcessor {

    String INPUT_STREAMS = "input-streams";
    String INPUT_REACTIVE = "input-reactive";
    String INPUT = "input";
    String OUTPUT = "output";

    @Output(OUTPUT)
    MessageChannel output();

    @Input(INPUT)
    SubscribableChannel input();

    @Input(INPUT_STREAMS)
    KStream<?, ?> inputStreams();

    @Input(INPUT_REACTIVE)
    Flux<?> inputReactive();
}
