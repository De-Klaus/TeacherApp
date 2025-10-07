package org.teacher.kafka.consumer;

public interface EventConsumer <T> {
    void eventConsumer(T event) throws Exception;
}
