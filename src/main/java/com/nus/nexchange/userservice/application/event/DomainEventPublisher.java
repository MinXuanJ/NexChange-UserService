package com.nus.nexchange.userservice.application.event;

public interface DomainEventPublisher {
    void publish(Object event);
}
