package main.service;

import main.api.response.TagResponse;
import main.api.response.jsonClasses.TagCount;
import main.api.response.jsonClasses.TagJson;
import main.model.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class TagService {

    @Autowired
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagResponse getDefaultTagResponse(){
        TagResponse tagResponse = new TagResponse();
        List<TagCount> tagCount = tagRepository.findDefaultTags();
        double maxCount = getMaxCount(tagCount);
        for (TagCount tag : tagCount) {
            tagResponse.addToTagList(new TagJson(tag.getName(), tag.getCount() / maxCount));
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
