package co.com.pragma.peoplems.kafka.adapter;

import co.com.pragma.peoplems.kafka.config.KafkaTopicsProperties;
import co.com.pragma.peoplems.model.event.AuthLoginEvent;
import co.com.pragma.peoplems.model.event.AuthLogoutEvent;
import co.com.pragma.peoplems.model.event.gateways.EventGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class EventPublisherAdapter implements EventGateway {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTopicsProperties topics;

    @Override
    public Mono<Void> publishAuthLoginAdmin(AuthLoginEvent event) {
        return Mono.fromFuture(kafkaTemplate.send(topics.getAuthLoginAdmin(), event)).then();
    }

    @Override
    public Mono<Void> publishGenericAuthLogin(AuthLoginEvent event) {
        return Mono.fromFuture(kafkaTemplate.send(topics.getGenericAuthLogin(), event)).then();
    }

    @Override
    public Mono<Void> publishGenericAuthLogout(AuthLogoutEvent event) {
        return Mono.fromFuture(kafkaTemplate.send(topics.getGenericAuthLogout(), event)).then();
    }
}
