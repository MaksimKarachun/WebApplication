package main.model.appEnum;

import lombok.Getter;

@Getter
public enum Vote {

  LIKE(1),
  DISLIKE(-1);

  private final int value;

  Vote(int value) {
    this.value = value;
  }
}
