package main.service;

import main.api.response.PostResponse;
import main.api.response.jsonClasses.PostJson;
import main.api.response.jsonClasses.UserJson;
import main.model.Post;
import main.model.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse getPosts(int offset, int limit, String mode){
        PostResponse postResponse = new PostResponse();
        List<Post> postList;
        switch (mode) {
            case "recent":
                postList = postRepository.findPostsByParamRecent(offset, limit);
                postResponse.setCount(postList.size());
                for (Post post : postList){
                    postResponse.addToPostList(new PostJson(
                            post.getId(),
                            post.getTime().getTime() / 1000,
                            new UserJson(post.getUser().getId(), post.getUser().getName()),
                            post.getTitle(),
                            post.getText(),
                            post.getLikeCount(),
                            post.getDislikeCount(),
                            post.getPostComments().size(),
                            post.getViewCount()));
                }
                break;

            case "popular":
                // TODO: 16.05.2021
                break;
            case "best":
                // TODO: 16.05.2021
                break;
            case "early":
                // TODO: 16.05.2021
                break;
        }
        return postResponse;
    }
}
