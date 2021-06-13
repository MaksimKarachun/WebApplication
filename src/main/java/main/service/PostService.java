package main.service;

import main.DTO.response.PostResponse;
import main.DTO.dtoObj.Post;
import main.DTO.dtoObj.User;
import main.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse getPosts(int offset, int limit, String mode){
        int pageNumber = offset / limit;
        Pageable pageWithPosts = PageRequest.of(pageNumber, limit);
        PostResponse postResponse = new PostResponse();
        switch (mode) {
            case "recent":
                getRecentPosts(postResponse, pageWithPosts);
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

    private void getRecentPosts(PostResponse postResponse, Pageable pageWithPosts){
        List<main.model.Post> postList = postRepository.findPostsByParamRecent(pageWithPosts);
        postResponse.setCount(postList.size());
        for (main.model.Post post : postList){
            postResponse.addToPostList(new Post(
                    post.getId(),
                    post.getTime().getTime() / 1000,
                    new User(post.getUser().getId(), post.getUser().getName()),
                    post.getTitle(),
                    post.getText(),
                    post.getLikeCount(),
                    post.getDislikeCount(),
                    post.getPostComments().size(),
                    post.getViewCount()));
        }
    }
}
