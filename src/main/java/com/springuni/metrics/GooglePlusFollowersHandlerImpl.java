package com.springuni.metrics;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GooglePlusFollowersHandlerImpl implements GooglePlusFollowersHandler {

  private final Plus plus;

  @Override
  public Integer handle(LocalDate payload, Map<String, Object> headers) {
    try {
      Person person = plus.people().get("107304850009258728229").setFields("circledByCount").execute();
      return person.getCircledByCount().intValue();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
