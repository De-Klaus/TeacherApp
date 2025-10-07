package org.teacher.kafka.producer;

public interface EventProducer<T> {
    void send(T event);
}