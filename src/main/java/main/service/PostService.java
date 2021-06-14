package main.service;

import main.DTO.response.PostResponse;
import main.DTO.dtoObj.Post;
import main.DTO.dtoObj.User;
import main.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse getPostsByParam(int offset, int limit, String mode){
        int pageNumber = offset / limit;
        Pageable pageWithPosts;
        List<main.model.Post> postList = new ArrayList<>();
        switch (mode) {
            case "recent":
                pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time").descending());
                postList = postRepository.findPostsByParamRecent(pageWithPosts);
                break;
            case "popular":
                pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("commentCount").descending());
                postList = postRepository.findPostsByParamPopular(pageWithPosts);
                break;
            case "best":
                pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("postLike").descending());
                postList = postRepository.findPostsByParamBest(pageWithPosts);
                break;
            case "early":
                pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time"));
                postList = postRepository.findPostsByParamRecent(pageWithPosts);
                break;
        }
        return prepareRequest(postList);
    }

    public PostResponse getPostsBySearch(int offset, int limit, String query){
        if (query.isEmpty() || query.isBlank()){
            return getPostsByParam(offset, limit, "recent");
        }
        else {
            int pageNumber = offset / limit;
            Pageable pageWithPosts;
            pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time"));
            List<main.model.Post> postList;
            postList = postRepository.findPostsByQuery(pageWithPosts, query);
            return prepareRequest(postList);
        }
    }

    private PostResponse prepareRequest(List<main.model.Post> postList){
        PostResponse postResponse = new PostResponse();
        int postListSize = postList.size();
        postResponse.setCount(postListSize);
        if (postListSize > 0) {
            for (main.model.Post post : postList) {
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
        else {
            postResponse.setPosts(new ArrayList<>());
        }
        return postResponse;
    }
}
