package main.dto.request;

import java.util.List;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPostRequest {

  private Long timestamp;
  private Boolean active;
  @Size(min = 3, message = "Длина заголовка поста менее 3 символа.")
  private String title;
  private List<String> tags;
  @Size(min = 50, message = "Длина теста поста менее 50 символов.")
  private String text;
}
