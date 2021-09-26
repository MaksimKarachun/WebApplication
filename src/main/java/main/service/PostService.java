package main.service;

import lombok.RequiredArgsConstructor;
import main.dto.request.AddPostRequest;
import main.dto.response.*;
import main.exception.PostNotFoundException;
import main.model.ModerationStatus;
import main.model.Post;
import main.model.PostComment;
import main.model.Tag;
import main.model.User;
import main.repository.PostRepository;
import main.repository.TagRepository;
import main.repository.UserRepository;
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

    private final UserRepository userRepository;

    private final TagRepository tagRepository;

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
        return preparePostResponse(postList, postRepository.getPostCount());
    }

    public PostResponse getPostsBySearch(int offset, int limit, String query){
        if (query.isEmpty() || query.isBlank()){
            return getPostsByParam(offset, limit, "recent");
        }
        else {
            int pageNumber = offset / limit;
            Pageable pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time"));
            List<Post> postList = postRepository.findPostsByQuery(pageWithPosts, query);
            return preparePostResponse(postList, postList.size());
        }
    }

    public PostResponse getPostsByDate(int offset, int limit, String date){
        int pageNumber = offset / limit;
        Pageable pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time"));
        List<Post> postList = postRepository.findPostsByDate(pageWithPosts, date);
        return preparePostResponse(postList, postList.size());
    }

    public PostResponse getPostsByTag(int offset, int limit, String tag){
        int pageNumber = offset / limit;
        Pageable pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time"));
        List<Post> postList = postRepository.findPostsByTag(pageWithPosts, tag);
        return preparePostResponse(postList, postList.size());
    }

    public PostByIdResponse getPostById(int id) throws PostNotFoundException{
        Post post = Optional.of(postRepository.getPostById(id)).orElseThrow(() -> new PostNotFoundException(id));
        post.incrementViewCount();
        postRepository.save(post);
        return preparePostByIdResponse(post);
    }

    public PostResponse getMyPosts(int offset, int limit, String status, String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        int pageNumber = offset / limit;
        Pageable pageWithPosts;
        List<main.model.Post> postList = new ArrayList<>();
        int count = 0;
        pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time").descending());
        switch (status) {
            case "pending":
                postList = postRepository.getPostsByUserPending(pageWithPosts, user.getId());
                count = postRepository.getCountPostsByUserPending(user.getId());
                break;
            case "inactive":
                postList = postRepository.getPostsByUserInactive(pageWithPosts, user.getId());
                count = postRepository.getCountPostsByUserInactive(user.getId());
                break;
            case "accepted":
                postList = postRepository.getPostsByUserAccepted(pageWithPosts, user.getId());
                count = postRepository.getCountPostsByUserDeclined(user.getId());
                break;
            case "declined":
                postList = postRepository.getPostsByUserDeclined(pageWithPosts, user.getId());
                count = postRepository.getCountPostsByUserDeclined(user.getId());
                break;
        }
        return preparePostResponse(postList, count);
    }

    public AddPostResponse addNewPost(AddPostRequest addPostRequest, String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        Post post = new Post();
        post.setActive(addPostRequest.getActive());
        post.setModerationStatus(ModerationStatus.NEW);
        post.setUser(user);
        post.setTime(new Date(addPostRequest.getTimestamp() * 1000));
        post.setTitle(addPostRequest.getTitle());
        post.setText(addPostRequest.getText());
        post.setViewCount(0);
        post.setTags(getTagList(addPostRequest.getTags()));
        postRepository.save(post);
        return new AddPostResponse(true);
    }

    public PostResponse getModerationPosts(int offset, int limit, String status, String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        int pageNumber = offset / limit;
        Pageable pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time").descending());
        List<main.model.Post> postList = new ArrayList<>();
        int count = 0;
        switch (status) {
            case "new":
                postList = postRepository.getModerationNewPosts(pageWithPosts);
                count = postRepository.getModerationNewPostsCount();
                break;
            case "declined":
                postList = postRepository.getModerationDeclinedPosts(pageWithPosts, user.getId());
                count = postRepository.getModerationDeclinedPostsCount(user.getId());
                break;
            case "accepted":
                postList = postRepository.getModerationAcceptedPosts(pageWithPosts, user.getId());
                count = postRepository.getModerationAcceptedPostsCount(user.getId());
                break;
        }
        return preparePostResponse(postList, count);
    }

    private PostResponse preparePostResponse(List<main.model.Post> postList, int postCount){
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

    private ArrayList<Tag> getTagList(List<String> tagsName) {
        ArrayList<Tag> tagList = new ArrayList<>();
        for (String tagName : tagsName) {
            Tag tag = tagRepository.findByName(tagName);
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                tagRepository.save(tag);
            }
            tagList.add(tag);
        }
        return tagList;
    }
}
