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
    public PostResponse postResponse(@RequestParam int offset, @RequestParam int limit, @RequestParam String mode){
        return postService.getPosts(offset, limit, mode);
    }
}
