package com.epam.esm.entity;

import java.util.Objects;

public class PageMeta {
    private int size;
    private int totalElements;
    private int totalPages;
    private int page;

    public PageMeta() {
    }

    public PageMeta(int size, int totalElements, int totalPages, int page) {
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageMeta pageMeta = (PageMeta) o;
        return size == pageMeta.size && totalElements == pageMeta.totalElements && totalPages == pageMeta.totalPages && page == pageMeta.page;
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, totalElements, totalPages, page);
    }

    @Override
    public String toString() {
        return "PageMeta{" +
                "size=" + size +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                ", number=" + page +
                '}';
    }

    public static class PageMetaBuilder {
        private final PageMeta pageMeta;

        public PageMetaBuilder() {
            pageMeta = new PageMeta();
        }

        public PageMeta.PageMetaBuilder setSize(int size) {
            pageMeta.setSize(size);
            return this;
        }

        public PageMeta.PageMetaBuilder setTotalElements(int totalElements) {
            pageMeta.setTotalElements(totalElements);
            return this;
        }

        public PageMeta.PageMetaBuilder setTotalPages(int totalPages) {
            pageMeta.setTotalPages(totalPages);
            return this;
        }

        public PageMeta.PageMetaBuilder setNumber(int number) {
            pageMeta.setPage(number);
            return this;
        }

        public PageMeta createPageMeta() {
            return pageMeta;
        }
    }
}
