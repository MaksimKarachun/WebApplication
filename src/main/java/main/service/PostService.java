package main.service;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import main.dto.request.AddPostCommentRequest;
import main.dto.request.AddPostRequest;
import main.dto.request.EditPostRequest;
import main.dto.response.*;
import main.exception.AddPostCommentException;
import main.exception.PostNotFoundException;
import main.model.ModerationStatus;
import main.model.Post;
import main.model.PostComment;
import main.model.Tag;
import main.model.User;
import main.repository.PostCommentRepository;
import main.repository.PostRepository;
import main.repository.TagRepository;
import main.repository.UserRepository;
import main.stringConst.StringConstant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  private final PostCommentRepository postCommentRepository;

  /**
   * Получение постов в зависимости от переданного параметра mode. Модификатор по умолчанию
   * "recent".
   */
  public PostResponse getPostsByParam(int offset, int limit, String mode) {
    int pageNumber = offset / limit;
    Pageable pageWithPosts;
    List<Post> postList = new ArrayList<>();
    Page<Post> postPage;
    long count;
    switch (mode) {
      case "popular":
        pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("commentCount").descending());
        Page<PostWithCount> postWithCountPage = Optional
            .of(postRepository.findPostsByParamPopular(pageWithPosts))
            .orElseThrow(() -> new RuntimeException("Не удалось получить данные из БД."));
        count = postWithCountPage.getTotalElements();
        postWithCountPage.toList().forEach(p -> postList.add(p.getPost()));
        break;
      case "best":
        pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("postLike").descending());
        Page<PostWithLike> postWithLikePage = Optional
            .of(postRepository.findPostsByParamBest(pageWithPosts))
            .orElseThrow(() -> new RuntimeException("Не удалось получить данные из БД."));
        count = postWithLikePage.getTotalElements();
        postWithLikePage.toList().forEach(p -> postList.add(p.getPost()));
        break;
      case "early":
        pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time"));
        postPage = Optional.of(postRepository.findPostsByParamRecent(pageWithPosts))
            .orElseThrow(() -> new RuntimeException("Не удалось получить данные из БД."));
        count = postPage.getTotalElements();
        postPage.forEach(postList::add);
        break;
      default:
        pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time").descending());
        postPage = Optional.of(postRepository.findPostsByParamRecent(pageWithPosts))
            .orElseThrow(() -> new RuntimeException("Не удалось получить данные из БД."));
        count = postPage.getTotalElements();
        postPage.forEach(postList::add);
        break;
    }
    return preparePostResponse(postList, count);
  }

  public PostResponse getPostsBySearch(int offset, int limit, String query) {
    if (query.isEmpty() || query.isBlank()) {
      return getPostsByParam(offset, limit, "recent");
    } else {
      int pageNumber = offset / limit;
      Pageable pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time"));
      List<Post> postList = Optional.of(postRepository.findPostsByQuery(pageWithPosts, query))
          .orElseThrow(() -> new RuntimeException("Не удалось получить данные из БД."));
      return preparePostResponse(postList, postList.size());
    }
  }

  public PostResponse getPostsByDate(int offset, int limit, String date) {
    int pageNumber = offset / limit;
    Pageable pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time"));
    List<Post> postList = Optional.of(postRepository.findPostsByDate(pageWithPosts, date))
        .orElseThrow(() -> new RuntimeException("Не удалось получить данные из БД."));
    return preparePostResponse(postList, postList.size());
  }

  public PostResponse getPostsByTag(int offset, int limit, String tag) {
    int pageNumber = offset / limit;
    Pageable pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time"));
    List<Post> postList = Optional.of(postRepository.findPostsByTag(pageWithPosts, tag))
        .orElseThrow(() -> new RuntimeException("Не удалось получить данные из БД."));
    return preparePostResponse(postList, postList.size());
  }

  // TODO: 06.10.2021 Уточнить необходимость кастомного исключения
  public PostByIdResponse getPostById(int id, Principal principal) {
    Post post = postRepository.getPostById(id);
    if (principal != null) {
      //проверка является ли пользователь автором поста или модератором
      if (post.getUser().getEmail().equals(principal.getName()) && !post.getUser().isModerator()) {
        post.incrementViewCount();
      }
    } else {
      post.incrementViewCount();
    }
    postRepository.save(post);
    return preparePostByIdResponse(post);
  }

  /**
   * Получение постов текущего пользователя. Модификатор по умолчанию "pending".
   */
  public PostResponse getMyPosts(int offset, int limit, String status, String userEmail) {
    User user = userRepository.findByEmail(userEmail);
    int pageNumber = offset / limit;
    Pageable pageWithPosts;
    Page<Post> postPage;
    pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time").descending());
    switch (status) {
      case "inactive":
        postPage = Optional.of(postRepository.getPostsByUserInactive(pageWithPosts, user.getId()))
            .orElseThrow(() -> new RuntimeException("Не удалось получить данные из БД."));
        break;
      case "published":
        postPage = Optional.of(postRepository.getPostsByUserPublished(pageWithPosts, user.getId()))
            .orElseThrow(() -> new RuntimeException("Не удалось получить данные из БД."));
        break;
      case "declined":
        postPage = postRepository.getPostsByUserDeclined(pageWithPosts, user.getId());
        break;
      default:
        postPage = Optional.of(postRepository.getPostsByUserPending(pageWithPosts, user.getId()))
            .orElseThrow(() -> new RuntimeException("Не удалось получить данные из БД."));
        break;
    }
    return preparePostResponse(postPage.toList(), postPage.getTotalElements());
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

  /**
   * Получение постов на модерацию.. Модификатор по умолчанию "new".
   */
  public PostResponse getModerationPosts(int offset, int limit, String status, String userEmail) {
    User user = userRepository.findByEmail(userEmail);
    int pageNumber = offset / limit;
    Pageable pageWithPosts = PageRequest.of(pageNumber, limit, Sort.by("time").descending());
    Page<Post> postPage;
    switch (status) {
      case "declined":
        postPage = Optional
            .of(postRepository.getModerationDeclinedPosts(pageWithPosts, user.getId()))
            .orElseThrow(() -> new RuntimeException("Не удалось получить данные из БД"));
        break;
      case "accepted":
        postPage = Optional
            .of(postRepository.getModerationAcceptedPosts(pageWithPosts, user.getId()))
            .orElseThrow(() -> new RuntimeException("Не удалось получить данные из БД."));
        break;
      default:
        postPage = Optional.of(postRepository.getModerationNewPosts(pageWithPosts))
            .orElseThrow(() -> new RuntimeException("Не удалось получить данные из БД."));
        break;
    }
    return preparePostResponse(postPage.toList(), postPage.getTotalElements());
  }

  public ResponseEntity<EditPostResponse> editPost(int id, EditPostRequest editPostRequest,
      String userMail) {
    Post post = Optional.of(postRepository.getPostById(id))
        .orElseThrow(() -> new PostNotFoundException(id));
    User user = Optional.of(userRepository.findByEmail(userMail))
        .orElseThrow(() -> new RuntimeException("Не удалось получить данные из БД."));
    if (!editPostRequest.getTitle().isEmpty()) {
      post.setTitle(editPostRequest.getTitle());
    }
    if (!editPostRequest.getText().isEmpty()) {
      post.setText(editPostRequest.getText());
    }
    if (!editPostRequest.getTags().isEmpty()) {
      post.setTags(getTagList(editPostRequest.getTags()));
    }
    if (!user.isModerator()) {
      post.setModerationStatus(ModerationStatus.NEW);
    }
    postRepository.save(post);
    return new ResponseEntity<>(new EditPostResponse(true), HttpStatus.OK);
  }

  public ResponseEntity<AddPostCommentResponse> addPostComment(AddPostCommentRequest request,
      String userEmail) {
    Post post = Optional.of(postRepository.getPostById(request.getPostId()))
        .orElseThrow(() -> new AddPostCommentException(StringConstant.POST_NOT_FOUND));
    User user = Optional.of(userRepository.findByEmail(userEmail))
        .orElseThrow(() -> new AddPostCommentException(StringConstant.USER_NOT_FOUND));
    AddPostCommentResponse response = new AddPostCommentResponse();
    PostComment comment = new PostComment();
    comment.setPost(post);
    comment.setParentId(request.getParentId());
    comment.setText(request.getText());
    comment.setUser(user);
    comment.setTime(new Date());
    int commentId = postCommentRepository.save(comment).getId();
    response.setId(commentId);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  private PostResponse preparePostResponse(List<main.model.Post> postList, long postCount) {
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
    } else {
      postResponse.setPosts(new ArrayList<>());
    }
    return postResponse;
  }

  private PostByIdResponse preparePostByIdResponse(Post post) {
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
          user.getPhoto());
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
