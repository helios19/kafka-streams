package com.ing.cloud.stream.listener;

import com.ing.cloud.stream.processor.KafkaStreamProcessor;
import com.ing.cloud.stream.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import mysqlcdc.test.RawTransaction.Envelope;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;


@Slf4j
@Component
public class RawTransactionListener {

    private KafkaReceiver kafkaReceiver;
    private TransactionService transactionService;

    public RawTransactionListener(TransactionService transactionService/*, KafkaReceiver kafkaReceiver*/) {
        this.transactionService = transactionService;
        this.kafkaReceiver = kafkaReceiver;
    }

    @Autowired
    private QueryableStoreRegistry queryableStoreRegistry;

//    @StreamListener
//    public void process(@Input(PersonBinding.PERSON_IN) KStream<String, String> events) {
//
//        events.foreach(((key, value) -> System.out.println("Key: " + key + "; Value: " + value)));
//    }



//    @Bean
//    @InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "10000", maxMessagesPerPoll = "1"))
//    public MessageSource<TimeInfo> timerMessageSource() {
//        return () -> MessageBuilder.withPayload(new TimeInfo(new Date().getTime()+"","Label")).build();
//    }





//    @EnableBinding(KafkaStreamsProcessorX.class)
//    public static class KafkaStreamsAggregateSampleApplication {
//
//        @StreamListener("input")
//        public void process(KStream<Object, DomainEvent> input) {
//            ObjectMapper mapper = new ObjectMapper();
//            Serde<DomainEvent> domainEventSerde = new JsonSerde<>( DomainEvent.class, mapper );
//
//            input
//                    .groupBy(
//                            (s, domainEvent) -> domainEvent.boardUuid,
//                            Serialized.with(null, domainEventSerde))
//                    .aggregate(gknost
//                            String::new,
//                            (s, domainEvent, board) -> board.concat(domainEvent.eventType),
//                            Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as("test-events-snapshots").withKeySerde(Serdes.String()).
//                                    withValueSerde(Serdes.String())
//                    );
//        }
//    }


//    @StreamListener(target = Sink.INPUT, condition = "payload.class.simpleName=='Dog'")
//    public void bark(Dog dog) {
//        // handle the message
//    }



//    @StreamListener(Processor.INPUT)
    public void processListener(Envelope envelope) {
        log.info("--------------------------------");
        log.info("Received in processListener method: " + envelope.toString());
        log.info("--------------------------------");
    }


//    @StreamListener
    public void processKStream(@Input(KafkaStreamProcessor.INPUT_STREAMS) KStream<String, Envelope> events) {

        log.info("--------------------------------");
        log.info("Received in processKStream method");
        log.info("--------------------------------");

        events.foreach(((key, value) -> System.out.println("Key: " + key + "; Value: " + value)));
    }


    @StreamListener
    public void processReactive(@Input(KafkaStreamProcessor.INPUT_REACTIVE) Flux<Envelope> envelopeFlux) {

        log.info("--------------------------------");
        log.info("Received in processReactive method: " + envelopeFlux);
        log.info("--------------------------------");

    }


//    @StreamListener(Sink.INPUT)
//    @SendTo(Source.OUTPUT)
//    public KStream<?, WordCount> process(KStream<Object, String> input) {
//
//        return input
//                .flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
//                .map((key, value) -> new KeyValue<>(value, value))
//                .groupByKey(Serialized.with(Serdes.String(), Serdes.String()))
//                .windowedBy(TimeWindows.of(30000))
//                .count(Materialized.as("WordCounts-1"))
//                .toStream()
//                .map((key, value) -> new KeyValue<>(null, new WordCount(key.key(), value, new Date(key.window().start()), new Date(key.window().end()))));
//    }

}

//@RestController
//public class FooController {
//
//    @RequestMapping("/events")
//    public String events() {
//
//        final ReadOnlyKeyValueStore<String, String> topFiveStore =
//                queryableStoreRegistry.getQueryableStoreType("test-events-snapshots", QueryableStoreTypes.<String, String>keyValueStore());
//        return topFiveStore.get("12345");
//    }
//}


//interface KafkaStreamsProcessorX {
//
//    @Input("input")
//    KStream<?, ?> input();
//}


//interface KStreamKTableBinding {
//
//    @Input("inputStream")
//    KStream<?, ?> inputStream();
//
//    @Input("inputTable")
//    KTable<?, ?> inputTable();
//}
