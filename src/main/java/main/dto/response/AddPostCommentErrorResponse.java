package main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPostCommentErrorResponse {

  private boolean result;
  private AddPostCommentErrorDTO errors;
}
