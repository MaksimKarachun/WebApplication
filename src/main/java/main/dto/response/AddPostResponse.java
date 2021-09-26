package main.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPostResponse {

  private Boolean result;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private AddPostErrorDTO errors;

  public AddPostResponse(boolean result) {
    this.result = result;
  }
}
