package com.springuni.metrics;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class YouTubeFollowersHandlerImpl implements YouTubeFollowersHandler {

  private final YouTube youTube;

  @Override
  public Integer handle(LocalDate payload, Map<String, Object> headers) {
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
