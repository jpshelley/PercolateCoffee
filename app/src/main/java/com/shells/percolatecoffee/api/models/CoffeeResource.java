package com.shells.percolatecoffee.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = false)
public class CoffeeResource {

    @JsonProperty("desc")
    public String desc;

    @JsonProperty("image_url")
    public String image_url;

    @JsonProperty("id")
    public String id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("last_updated_at")
    public String last_updated_at;

}
