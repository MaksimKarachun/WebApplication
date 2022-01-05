package main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarResponse {

    private List<Integer> years;
    private HashMap<String, Integer> posts;

    public void addToPosts(String date, Integer count){
        if (posts == null) {
            posts = new HashMap<>();
        }
        posts.put(date, count);
    }
}
