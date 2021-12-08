package main.service;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import main.dto.response.PostInfo;
import main.dto.response.CalendarResponse;
import main.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

  private final PostRepository postRepository;

  public CalendarResponse getPostByYear(String year) {
    CalendarResponse calendarResponse = new CalendarResponse();
    calendarResponse.setYears(postRepository.getPostsYears(new Date()));
    List<PostInfo> postInfoList = postRepository.getPostsByYear(year, new Date());
    postInfoList.forEach(e -> calendarResponse.addToPosts(e.getDate(), e.getPostsCount()));
    return calendarResponse;
  }
}
