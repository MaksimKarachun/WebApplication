package main.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeResponse {

  private boolean result;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private PasswordChangeError errors;
}
