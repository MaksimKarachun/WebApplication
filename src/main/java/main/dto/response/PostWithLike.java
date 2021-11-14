package main.dto.response;

import main.model.Post;

public interface PostWithLike {

  Post getPost();
  Long getPostLike();
}
