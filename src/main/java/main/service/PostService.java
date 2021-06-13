package main.service;

import main.DTO.response.PostResponse;
import main.DTO.dtoObj.Post;
import main.DTO.dtoObj.User;
import main.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        PostResponse postResponse = new PostResponse();
        Pageable pageWithPosts;
        List<main.model.Post> postList;
        switch (mode) {
            case "recent":
                pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time").descending());
                postList = postRepository.findPostsByParamRecent(pageWithPosts);
                prepareRequest(postList);
                break;
            case "popular":
                pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("commentCount").descending());
                postList = postRepository.findPostsByParamPopular(pageWithPosts);
                prepareRequest(postList);
                break;
            case "best":
                pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("postLike").descending());
                postList = postRepository.findPostsByParamBest(pageWithPosts);
                prepareRequest(postList);
                break;
            case "early":
                pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time"));
                postList = postRepository.findPostsByParamRecent(pageWithPosts);
                prepareRequest(postList);
                break;
        }
        return postResponse;
    }

    private void prepareRequest(List<main.model.Post> postList){
        PostResponse postResponse = new PostResponse();
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
