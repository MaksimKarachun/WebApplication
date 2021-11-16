package main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsResponse {

  long postsCount;
  long likesCount;
  long dislikesCount;
  long viewsCount;
  long firstPublication;
}
