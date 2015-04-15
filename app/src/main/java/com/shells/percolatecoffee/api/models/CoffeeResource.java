package com.shells.percolatecoffee.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CoffeeResource {

    @JsonProperty("desc")
    public String desc;

    @JsonProperty("image_url")
    public String image_url;

    @JsonProperty("id")
    public String id;

    @JsonProperty("name")
    public String name;

}
