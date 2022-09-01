package com.simpolab.client_manager.group;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Group {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name + "(" + id +")";
    }

    @JsonCreator
    public Group(@JsonProperty("id") String id,
                 @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public Group(@JsonProperty("name") String name){
        this.name = name;
    }
}
