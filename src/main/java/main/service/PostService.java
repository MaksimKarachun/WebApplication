package main.service;

import lombok.RequiredArgsConstructor;
import main.DTO.dtoObj.*;
import main.DTO.response.PostByIdResponse;
import main.DTO.response.PostResponse;
import main.exception.PostNotFoundException;
import main.model.Post;
import main.model.PostComment;
import main.model.Tag;
import main.model.User;
import main.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

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
        return prepareResponse(postList, postRepository.getPostCount());
    }

    public PostResponse getPostsBySearch(int offset, int limit, String query){
        if (query.isEmpty() || query.isBlank()){
            return getPostsByParam(offset, limit, "recent");
        }
        else {
            int pageNumber = offset / limit;
            Pageable pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time"));
            List<Post> postList = postRepository.findPostsByQuery(pageWithPosts, query);
            return prepareResponse(postList, postList.size());
        }
    }

    public PostResponse getPostsByDate(int offset, int limit, String date){
        int pageNumber = offset / limit;
        Pageable pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time"));
        List<Post> postList = postRepository.findPostsByDate(pageWithPosts, date);
        return prepareResponse(postList, postList.size());
    }

    public PostResponse getPostsByTag(int offset, int limit, String tag){
        int pageNumber = offset / limit;
        Pageable pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time"));
        List<Post> postList = postRepository.findPostsByTag(pageWithPosts, tag);
        return prepareResponse(postList, postList.size());
    }

    public PostByIdResponse getPostById(int id) throws PostNotFoundException{
        Post post = Optional.of(postRepository.getPostById(id)).orElseThrow(() -> new PostNotFoundException(id));
        post.incrementViewCount();
        postRepository.save(post);
        return preparePostByIdResponse(post);
    }

    private PostResponse prepareResponse(List<main.model.Post> postList, int postCount){
        PostResponse postResponse = new PostResponse();
        postResponse.setCount(postCount);
        if (postCount > 0) {
            for (main.model.Post post : postList) {
                postResponse.addToPostList(new PostDTO(
                        post.getId(),
                        post.getTime().getTime() / 1000,
                        new UserDTO(post.getUser().getId(), post.getUser().getName()),
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

    private PostByIdResponse preparePostByIdResponse(Post post){
        PostByIdResponse postByIdResponse = new PostByIdResponse();
        postByIdResponse.setId(post.getId());
        postByIdResponse.setTimestamp(new Date().getTime() / 1000);
        postByIdResponse.setActive(post.isActive());
        postByIdResponse.setUser(new UserDTO(post.getUser().getId(),
                                             post.getUser().getName()));
        postByIdResponse.setTitle(post.getTitle());
        postByIdResponse.setText(post.getText());
        postByIdResponse.setLikeCount(post.getLikeCount());
        postByIdResponse.setDislikeCount(post.getDislikeCount());
        postByIdResponse.setViewCount(post.getViewCount());
        postByIdResponse.setComments(new ArrayList<>());

        //Добавление комментариев поста в postByIdResponse в нужном формате: PostComment -> CommentDTO
        for (PostComment postComment : post.getPostComments()) {
            User user = postComment.getUser();
            // TODO: 18.07.2021
            UserForCommentDTO userForCommentDTO = new UserForCommentDTO(user.getId(),
                                                                        user.getName(),
                                                                        "photo");
            postByIdResponse.getComments().add(new CommentDTO(postComment.getId(),
                                                              (postComment.getTime().getTime() / 1000),
                                                              postComment.getText(),
                                                              userForCommentDTO));
        }

        //Добавление имен тегов поста в postByIdResponse в нужном формате: Tag -> Tag.getName()
        List<String> tagName = post.getTags().stream()
                                             .map(Tag::getName)
                                             .collect(Collectors.toList());
        postByIdResponse.setTags(new ArrayList<>());
        postByIdResponse.getTags().addAll(tagName);
       return postByIdResponse;
    }
}
