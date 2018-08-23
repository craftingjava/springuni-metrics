package com.springuni.metrics;

import java.time.LocalDate;
import java.util.Map;
import org.springframework.integration.handler.GenericHandler;

public interface GooglePlusFollowersHandler extends GenericHandler<LocalDate> {

  @Override
  Integer handle(LocalDate payload, Map<String, Object> headers);

}
