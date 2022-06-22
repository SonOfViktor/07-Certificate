package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Page<E> extends RepresentationModel<Page<E>> {
    private Iterable<E> entities;
    private PageMeta pageMeta;
}
