package com.springuni.metrics;

import static com.springuni.metrics.SocialNetwork.FACEBOOK;

import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Page;

public class FacebookFollowerCountFetcherImpl extends AbstractSocialFollowerCountFetcher {

  private final FacebookClient facebookClient;

  public FacebookFollowerCountFetcherImpl(FacebookClient facebookClient) {
    super(FACEBOOK);
    this.facebookClient = facebookClient;
  }

  @Override
  protected int fetchFollowers() {
    Page page = facebookClient
        .fetchObject("springuni", Page.class, Parameter.with("fields", "fan_count"));

    return page.getFanCount().intValue();
  }

}
