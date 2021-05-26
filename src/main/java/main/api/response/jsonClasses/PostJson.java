package main.api.response.jsonClasses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class PostJson {

    @JsonProperty
    private int id;
    @JsonProperty
    private Long timestamp;
    @JsonProperty("user")
    private UserJson userJson;
    @JsonProperty
    private String title;
    @JsonProperty
    private String announce;
    @JsonProperty
    private int likeCount;
    @JsonProperty
    private int dislikeCount;
    @JsonProperty
    private int commentCount;
    @JsonProperty
    private int viewCount;

    public PostJson(int id, Long timestamp, UserJson userJson, String title, String announce, int likeCount, int dislikeCount, int commentCount, int viewCount) {
        this.id = id;
        this.timestamp = timestamp;
        this.userJson = userJson;
        this.title = title;
        this.announce = announce;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
    }
}