package main.controller;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import main.dto.request.AddPostRequest;
import main.dto.request.EditPostRequest;
import main.dto.request.PostVoteRequest;
import main.dto.response.AddPostResponse;
import main.dto.response.EditPostResponse;
import main.dto.response.PostVoteResponse;
import main.dto.response.PostByIdResponse;
import main.dto.response.PostResponse;
import main.exception.AddNewPostValidationException;
import main.exception.EditPostValidationException;
import main.exception.PostNotFoundException;
import main.service.PostService;
import main.service.PostVoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import static main.model.appEnum.Vote.LIKE;
import static main.model.appEnum.Vote.DISLIKE;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

  private final PostService postService;

  private final PostVoteService postVoteService;

  @GetMapping
  public PostResponse postsByParam(@RequestParam int offset, @RequestParam int limit,
      @RequestParam String mode) {
    return postService.getPostsByParam(offset, limit, mode);
  }

  @PostMapping
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<AddPostResponse> addPost(@Valid @RequestBody AddPostRequest addPostRequest,
      Errors errors, Principal principal) throws AddNewPostValidationException {
    if (errors.hasErrors()) {
      throw new AddNewPostValidationException(errors);
    }
    return new ResponseEntity<>(postService.addNewPost(addPostRequest, principal.getName()),
        HttpStatus.OK);
  }

  @GetMapping("/search")
  public PostResponse postsBySearch(@RequestParam int offset, @RequestParam int limit,
      @RequestParam String query) {
    return postService.getPostsBySearch(offset, limit, query);
  }

  @GetMapping("/byDate")
  public PostResponse postsByDate(@RequestParam int offset, @RequestParam int limit,
      @RequestParam String date) {
    return postService.getPostsByDate(offset, limit, date);
  }

  @GetMapping("/byTag")
  public PostResponse postsByTag(@RequestParam int offset, @RequestParam int limit,
      @RequestParam String tag) {
    return postService.getPostsByTag(offset, limit, tag);
  }

  @GetMapping("/{id}")
  public PostByIdResponse postsById(@PathVariable int id, Principal principal)
      throws PostNotFoundException {
    return postService.getPostById(id, principal);
  }

  @GetMapping("/my")
  @PreAuthorize("hasAuthority('user:write')")
  public PostResponse myPosts(@RequestParam int offset, @RequestParam int limit,
      @RequestParam String status, Principal principal) {
    return postService.getMyPosts(offset, limit, status, principal.getName());
  }

  @GetMapping("/moderation")
  @PreAuthorize("hasAuthority('user:moderate')")
  public PostResponse addPost(@RequestParam int offset, @RequestParam int limit,
      @RequestParam String status, Principal principal) {
    return postService.getModerationPosts(offset, limit, status, principal.getName());
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<EditPostResponse> editPost(@PathVariable int id,
      @Valid @RequestBody EditPostRequest editPostRequest, Errors errors, Principal principal)
      throws EditPostValidationException {
    if (errors.hasErrors()) {
      throw new EditPostValidationException(errors);
    }
    return postService.editPost(id, editPostRequest, principal.getName());
  }

  @PostMapping("/like")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<PostVoteResponse> likePost(@RequestBody PostVoteRequest request,
      Principal principal) {
    return postVoteService.votePost(request, principal.getName(), LIKE);
  }

  @PostMapping("/dislike")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<PostVoteResponse> dislikePost(@RequestBody PostVoteRequest request,
      Principal principal) {
    return postVoteService.votePost(request, principal.getName(), DISLIKE);
  }
}
