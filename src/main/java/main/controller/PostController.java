package main.controller;

import lombok.RequiredArgsConstructor;
import main.DTO.response.PostByIdResponse;
import main.DTO.response.PostResponse;
import main.exception.PostNotFoundException;
import main.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/api/post/byDate")
    public PostResponse postsByDate(@RequestParam int offset, @RequestParam int limit, @RequestParam String date){
        return postService.getPostsByDate(offset, limit, date);
    }

    @GetMapping("/api/post/byTag")
    public PostResponse postsByTag(@RequestParam int offset, @RequestParam int limit, @RequestParam String tag){
        return postService.getPostsByTag(offset, limit, tag);
    }

    @GetMapping("/api/post/{id}")
    public PostByIdResponse postsByTag(@PathVariable int id) throws PostNotFoundException {
        return postService.getPostById(id);
    }
}
