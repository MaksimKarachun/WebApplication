package main.controller;

import lombok.RequiredArgsConstructor;
import main.dto.response.CalendarResponse;
import main.service.CalendarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/api/calendar")
    public CalendarResponse postsByParam(@RequestParam String year){
        return calendarService.getPostByYear(year);
    }
}
