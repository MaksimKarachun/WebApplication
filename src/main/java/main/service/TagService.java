package main.service;

import main.DTO.response.TagResponse;
import main.DTO.dtoObj.TagCount;
import main.DTO.dtoObj.Tag;
import main.repository.TagRepository;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagResponse getDefaultTagResponse(){
        TagResponse tagResponse = new TagResponse();
        List<TagCount> tagCount = tagRepository.findDefaultTags();
        for (TagCount tag : tagCount) {
            tagResponse.addToTagList(new Tag(tag.getName(), tag.getCount() / getMaxCount(tagCount)));
        }
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
