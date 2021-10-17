package main.projectEnum;

import lombok.Getter;

@Getter
public enum ModerateDecision {

  ACCEPT("accept"),
  DECLINE("decline");

  private String name;

  ModerateDecision(String name) {
    this.name = name;
  }
}
