package com.springuni.metrics;

import java.time.LocalDate;
import java.util.Map;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.Message;

public interface SocialFollowerCountFetcher extends GenericHandler<LocalDate> {

  @Override
  Message<SocialFollowerCount> handle(LocalDate payload, Map<String, Object> headers);

}
