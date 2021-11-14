package main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadImageErrorResponse {

  private boolean result;
  private UploadImageErrorDto errors;
}
