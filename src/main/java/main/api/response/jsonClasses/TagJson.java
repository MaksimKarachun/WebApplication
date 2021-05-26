package main.api.response.jsonClasses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TagJson {

    @JsonProperty
    String  name;
    @JsonProperty
    Double weight;

    public TagJson(String name, Double weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
