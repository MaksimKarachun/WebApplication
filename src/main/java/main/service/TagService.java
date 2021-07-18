package main.service;

import lombok.RequiredArgsConstructor;
import main.DTO.response.TagResponse;
import main.DTO.dtoObj.TagCount;
import main.DTO.dtoObj.TagDTO;
import main.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public TagResponse getDefaultTagResponse(){
        TagResponse tagResponse = new TagResponse();
        List<TagCount> tagCount = tagRepository.findDefaultTags();
        tagCount.forEach( e -> tagResponse.addToTagList(new TagDTO(e.getName(), e.getCount() / getMaxCount(tagCount))));
        return tagResponse;
    }

    private double getMaxCount(List<TagCount> tags){
        double maxCount = 0;
        for (TagCount tag : tags){
            if (tag.getCount() > maxCount){
                maxCount = tag.getCount();
            }
        }
        return maxCount;
    }
}
