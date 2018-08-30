package com.springuni.metrics.follower.google;

import static com.springuni.metrics.follower.SocialNetwork.GOOGLE_PLUS;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.springuni.metrics.follower.AbstractSocialFollowerCountFetcher;
import java.io.IOException;

public class GooglePlusFollowerCountFetcherImpl extends AbstractSocialFollowerCountFetcher {

  private final Plus plus;

  public GooglePlusFollowerCountFetcherImpl(Plus plus) {
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
