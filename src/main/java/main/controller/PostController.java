package main.controller;

import lombok.RequiredArgsConstructor;
import main.DTO.response.PostResponse;
import main.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/api/post")
    public PostResponse postsByParam(@RequestParam int offset, @RequestParam int limit, @RequestParam String mode){
        return postService.getPostsByParam(offset, limit, mode);
    }

    @GetMapping("/api/post/search")
    public PostResponse postsBySearch(@RequestParam int offset, @RequestParam int limit, @RequestParam String query){
        return postService.getPostsBySearch(offset, limit, query);
    }
}
