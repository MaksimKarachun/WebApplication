package main.dto.response;

import main.model.Post;

public interface PostWithCount {

  Post getPost();
  Long getCommentCount();

}
