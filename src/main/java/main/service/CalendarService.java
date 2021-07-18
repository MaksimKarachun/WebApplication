package main.service;

import lombok.RequiredArgsConstructor;
import main.DTO.dtoObj.PostInfo;
import main.DTO.response.CalendarResponse;
import main.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final PostRepository postRepository;

    public CalendarResponse getPostByYear(String year){
        CalendarResponse calendarResponse = new CalendarResponse();
        calendarResponse.setYears(postRepository.getPostsYears());
        List<PostInfo> postInfoList = postRepository.getPostsByYear(year);
        postInfoList.forEach(e -> calendarResponse.addToPosts(e.getDate(), e.getPostsCount()));
        return calendarResponse;
    }
}
