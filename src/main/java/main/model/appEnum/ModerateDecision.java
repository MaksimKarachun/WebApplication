package main.model.appEnum;

import lombok.Getter;

@Getter
public enum ModerateDecision {

  ACCEPT("accept"),
  DECLINE("decline");

  private final String name;

  ModerateDecision(String name) {
    this.name = name;
  }
}
