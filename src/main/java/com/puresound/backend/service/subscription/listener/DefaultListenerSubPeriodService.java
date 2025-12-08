package com.puresound.backend.service.subscription.listener;

import com.puresound.backend.entity.jpa.subscription.listener.ListenerSubPeriod;
import com.puresound.backend.repository.jpa.subscription.listener.ListenerSubPeriodRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class DefaultListenerSubPeriodService implements ListenerSubPeriodService {
    ListenerSubPeriodRepository repository;

    @Transactional
    public ListenerSubPeriod getCurrentByListenerId(String listenerId) {
        return repository
                .findCurrentByListenerId(listenerId)
                .orElse(null);
    }
}
