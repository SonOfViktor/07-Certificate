package com.epam.esm.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class Tag {
    private int tagId;

    @NotNull
    @Size(min = 2, max = 45)
    private String name;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag(int tagId, String name) {
        this.tagId = tagId;
        this.name = name;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return tagId == tag.tagId && Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, name);
    }

    @Override
    public String toString() {
        return "Tag " + tagId + " " + name;
    }
}
