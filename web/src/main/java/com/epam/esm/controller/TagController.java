package com.epam.esm.controller;

import com.epam.esm.assembler.TagModelAssembler;
import com.epam.esm.entity.Page;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/tags")
@Validated
public class TagController {
    public static final String ALL_TAGS = "all_tags";
    public static final String POPULAR_TAGS = "popular_tags";
    public static final String ALL_GIFT_CERTIFICATES = "all_gift_certificates";
    private final TagService tagService;
    private final TagModelAssembler tagAssembler;

    @Autowired
    public TagController(TagService tagService, TagModelAssembler tagAssembler) {
        this.tagService = tagService;
        this.tagAssembler = tagAssembler;
    }

    @GetMapping
    public Page<EntityModel<Tag>> showAllTags(@RequestParam(required = false, defaultValue = "1") Integer page,
                                              @RequestParam(required = false, defaultValue = "5") Integer limit) {
        Page<Tag> tags = tagService.findAllTags(page, limit);

        return tagAssembler.toPageModel(tags)
                .add(linkTo(methodOn(TagController.class).showMostPopularHighestPriceTag()).withRel(POPULAR_TAGS))
                .add(linkTo(GiftCertificateController.class).withRel(ALL_GIFT_CERTIFICATES));
    }

    @GetMapping("/{id}")
    public EntityModel<Tag> showTag(@PathVariable @Positive int id) {
        Tag tag = tagService.findTagById(id);

        return tagAssembler.toModel(tag)
                .add(linkTo(TagController.class).withRel(ALL_TAGS));
    }

    @GetMapping("/highest")
    public CollectionModel<EntityModel<Tag>> showMostPopularHighestPriceTag() {
        List<Tag> tags = tagService.findMostPopularHighestPriceTag();

        return tagAssembler.toCollectionModel(tags).add(
                linkTo(methodOn(TagController.class)
                        .showMostPopularHighestPriceTag())
                        .withSelfRel(),
                linkTo(TagController.class).withRel(ALL_TAGS));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<Tag> addTag(@Valid @RequestBody Tag tag) {
        Tag newTag = tagService.addTag(tag);

        return tagAssembler.toModel(newTag).add(linkTo(TagController.class).withRel(ALL_TAGS));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable @Positive int id) {
        tagService.deleteTag(id);
    }
}
