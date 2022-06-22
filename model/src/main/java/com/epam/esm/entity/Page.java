package com.epam.esm.entity;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class Page<E> extends RepresentationModel<Page<E>> {
    Iterable<E> entities;
    PageMeta pageMeta;

    public Page(Iterable<E> entities, PageMeta pageMeta) {
        this.entities = entities;
        this.pageMeta = pageMeta;
    }

    public Iterable<E> getEntities() {
        return entities;
    }

    public void setEntities(Iterable<E> entities) {
        this.entities = entities;
    }

    public PageMeta getPageMeta() {
        return pageMeta;
    }

    public void setPageMeta(PageMeta pageMeta) {
        this.pageMeta = pageMeta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page<?> page = (Page<?>) o;
        return Objects.equals(entities, page.entities) && Objects.equals(pageMeta, page.pageMeta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entities, pageMeta);
    }

    @NonNull
    @Override
    public String toString() {
        return "Page{" +
                "entities=" + entities +
                ", pageMeta=" + pageMeta +
                '}';
    }
}
