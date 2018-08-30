package com.springuni.metrics.core.model;

import java.time.LocalDate;

public abstract class AbstractRecord {

  private final long id;
  private final LocalDate effDate;

  public AbstractRecord(long id, LocalDate effDate) {
    this.id = id;
    this.effDate = effDate;
  }

  public long getId() {
    return id;
  }

  public LocalDate getEffDate() {
    return effDate;
  }

}
