package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.geometry.Pos;
import main.api.response.jsonClasses.PostJson;

import java.util.ArrayList;
import java.util.List;

public class PostResponse {

    @JsonProperty
    private int count;
    @JsonProperty
    private List<PostJson> posts;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PostJson> getPosts() {
        return posts;
    }

    public void setPosts(List<PostJson> posts) {
        this.posts = posts;
    }

    public void addToPostList(PostJson postJson){
        if (posts == null) {
            posts = new ArrayList<>();
        }
        posts.add(postJson);
    }
}
