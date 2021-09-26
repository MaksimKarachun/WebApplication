package main.controller;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import main.dto.request.AddPostRequest;
import main.dto.response.AddPostResponse;
import main.dto.response.PostByIdResponse;
import main.dto.response.PostResponse;
import main.exception.PostNotFoundException;
import main.service.PostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/api/post")
    public PostResponse postsByParam(@RequestParam int offset, @RequestParam int limit,
        @RequestParam String mode){
        return postService.getPostsByParam(offset, limit, mode);
    }

    @GetMapping("/api/post/search")
    public PostResponse postsBySearch(@RequestParam int offset, @RequestParam int limit,
        @RequestParam String query){
        return postService.getPostsBySearch(offset, limit, query);
    }

    @GetMapping("/api/post/byDate")
    public PostResponse postsByDate(@RequestParam int offset, @RequestParam int limit,
        @RequestParam String date){
        return postService.getPostsByDate(offset, limit, date);
    }

    @GetMapping("/api/post/byTag")
    public PostResponse postsByTag(@RequestParam int offset, @RequestParam int limit,
        @RequestParam String tag){
        return postService.getPostsByTag(offset, limit, tag);
    }

    @GetMapping("/api/post/{id}")
    public PostByIdResponse postsByTag(@PathVariable int id) throws PostNotFoundException {
        return postService.getPostById(id);
    }

    @GetMapping("/api/post/my")
    @PreAuthorize("hasAuthority('user:write')")
    public PostResponse myPosts(@RequestParam int offset, @RequestParam int limit,
        @RequestParam String status, Principal principal) {
        return postService.getMyPosts(offset, limit, status, principal.getName());
    }

    @PostMapping("/api/post")
    @PreAuthorize("hasAuthority('user:write')")
    public AddPostResponse addPost(@RequestBody AddPostRequest addPostRequest, Principal principal) {
        return postService.addNewPost(addPostRequest, principal.getName());
    }

    @GetMapping("/api/post/moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public PostResponse addPost(@RequestParam int offset, @RequestParam int limit,
        @RequestParam String status, Principal principal) {
        return postService.getModerationPosts(offset, limit, status, principal.getName());
    }
}
