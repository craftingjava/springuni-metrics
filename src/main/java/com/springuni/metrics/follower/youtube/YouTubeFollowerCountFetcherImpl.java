package com.springuni.metrics.follower.youtube;

import static com.springuni.metrics.follower.SocialNetwork.YOUTUBE;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.springuni.metrics.follower.AbstractSocialFollowerCountFetcher;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YouTubeFollowerCountFetcherImpl extends AbstractSocialFollowerCountFetcher {

  private final YouTube youTube;

  public YouTubeFollowerCountFetcherImpl(YouTube youTube) {
    super(YOUTUBE);
    this.youTube = youTube;
  }

  @Override
  protected int fetchFollowers() {
    try {
      YouTube.Channels.List request = youTube.channels()
          .list("statistics")
          .setId("UC4v5ZYpefzOQu3iMP4NPgXw");

      ChannelListResponse response = request.execute();

      return response.getItems().get(0).getStatistics().getSubscriberCount().intValue();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
