package main.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditPostRequest {

  private Long timestamp;
  private boolean active;
  @Size(min = 3, message = "Загловок поста короче 3 симоволов.")
  private String title;
  private List<String> tags;
  @Size(min = 50, message = "Текст поста короче 50 симоволов.")
  private String text;

}
