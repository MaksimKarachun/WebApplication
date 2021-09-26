package main.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPostRequest {

  private Long timestamp;
  private Boolean active;
  private String title;
  private List<String> tags;
  private String text;
}
