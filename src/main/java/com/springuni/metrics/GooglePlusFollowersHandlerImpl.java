package com.springuni.metrics;

import static com.springuni.metrics.SocialNetwork.GOOGLE_PLUS;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import java.io.IOException;

public class GooglePlusFollowersHandlerImpl extends AbstractSocialFollowersHandler {

  private final Plus plus;

  public GooglePlusFollowersHandlerImpl(Plus plus) {
    super(GOOGLE_PLUS);
    this.plus = plus;
  }

  @Override
  protected int fetchFollowers() {
    try {
      Person person = plus.people()
          .get("107304850009258728229")
          .setFields("circledByCount")
          .execute();
      return person.getCircledByCount().intValue();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
