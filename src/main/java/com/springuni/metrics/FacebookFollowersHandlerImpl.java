package com.springuni.metrics;

import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Page;
import java.time.LocalDate;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FacebookFollowersHandlerImpl implements FacebookFollowersHandler {

  private final FacebookClient facebookClient;

  @Override
  public Integer handle(LocalDate payload, Map<String, Object> headers) {
    Page page = facebookClient
        .fetchObject("springuni", Page.class, Parameter.with("fields", "fan_count"));

    return page.getFanCount().intValue();
  }

}
