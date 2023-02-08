package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.specification.GiftCertificateSpecification;
import com.epam.esm.dto.FileImageDto;
import com.epam.esm.entity.Category;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateFilter;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.ImageService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.io.File;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {
    private static final String IMAGE_FILE_NAME = "certificate" + File.separator + "image2023-01-05T00-00-00.001.jpg";
    private static final Clock clock = Clock.fixed(Instant.parse("2023-01-05T00:00:00.001123Z"), ZoneOffset.UTC);
    private Page<GiftCertificate> certificates;
    private List<GiftCertificate> certificateList;
    private Set<Tag> tags;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Mock
    private GiftCertificateDao giftCertificateDao;

    @Mock
    private ImageService imageService;

    @Mock
    private GiftCertificateSpecification spec;

    private static MockedStatic<Clock> mockedClock;

    @BeforeAll
    static void initClock() {
        mockedClock = mockStatic(Clock.class);
        mockedClock.when(Clock::systemDefaultZone).thenReturn(clock);
    }

    @AfterAll
    static void cleanClock() {
        mockedClock.close();
    }

    @BeforeEach
    void init() {
        Pageable pageable = PageRequest.of(0, 10);
        certificateList = List.of(new GiftCertificate(), new GiftCertificate());
        certificateList.forEach(cert -> cert.setName("certificate"));

        certificates = new PageImpl<>(certificateList, pageable, 2);
        tags = Set.of(Tag.builder().tagId(1).name("tag1").build(),
                Tag.builder().tagId(2).name("tag2").build());
    }

    @Test
    void testAddGiftCertificate() {
        GiftCertificate expected = GiftCertificate.builder().name("image").build();

        when(giftCertificateDao.save(any(GiftCertificate.class))).thenReturn(expected);
        GiftCertificate actual = giftCertificateService
                .addGiftCertificate(expected, new FileImageDto(null, ".jpg"));

        verify(imageService).upload(IMAGE_FILE_NAME, null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testFindAllCertificates() {
        when(giftCertificateDao.findAll(PageRequest.of(0, 10))).thenReturn(certificates);

        Page<GiftCertificate> actual = giftCertificateService.findAllCertificates(PageRequest.of(0, 10));

        assertEquals(certificates, actual);
    }

    @Test
    void testFindAllCertificatesNotExistedPage() {
        when(giftCertificateDao.findAll(PageRequest.of(100, 10))).thenReturn(Page.empty());

        PageRequest pageable = PageRequest.of(100, 10);

        assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.findAllCertificates(pageable));
    }

    @Test
    void testFindCertificatesWithParams() {
        GiftCertificateFilter filter =  new GiftCertificateFilter(List.of("food"), "e",
                 "e", "Food");
        Specification<GiftCertificate> trueSpecification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        when(spec.hasTags(List.of("food"))).thenReturn(trueSpecification);
        when(spec.hasCategory("Food")).thenReturn(trueSpecification);
        when(spec.hasName("e")).thenReturn(trueSpecification);
                when(spec.hasDescription("e")).thenReturn(trueSpecification);
        when(giftCertificateDao.findAll(ArgumentMatchers.<Specification<GiftCertificate>>any(), eq(PageRequest.of(0, 10))))
                .thenReturn(certificates);

        Page<GiftCertificate> actual = giftCertificateService.findCertificatesWithParams(filter, PageRequest.of(0, 10));

        assertEquals(certificates, actual);
    }

    @Test
    void testFindCertificatesWithParamsEmptyResult() {
        GiftCertificateFilter filter =  new GiftCertificateFilter(List.of("food"), "e",
                "e", "Food");

        Pageable pageable = Pageable.unpaged();

        when(giftCertificateDao.findAll(ArgumentMatchers.<Specification<GiftCertificate>>any(), eq(Pageable.unpaged())))
                .thenReturn(Page.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                giftCertificateService.findCertificatesWithParams(filter, pageable));
    }

    @Test
    void testFindCertificateById() {
        GiftCertificate expected = certificateList.get(0);
        when(giftCertificateDao.findById(anyInt())).thenReturn(Optional.of(expected));

        GiftCertificate actual = giftCertificateService.findCertificateById(1);

        assertEquals(expected, actual);
    }

    @Test
    void testNotFindCertificateById() {
        when(giftCertificateDao.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.findCertificateById(1));
    }

    @Test
    void testUpdateGiftCertificate() {
        GiftCertificate certificate = GiftCertificate.builder()
                .name("Abra-cadabra")
                .description("description")
                .createDate(LocalDateTime.now(clock))
                .build();
        GiftCertificate newCertificate = GiftCertificate.builder()
                .name("image")
                .price(new BigDecimal("40"))
                .description("new description")
                .duration(20)
                .tags(tags)
                .category(Category.builder().name("Food").build())
                .build();

        when(giftCertificateDao.findById(anyInt())).thenReturn(Optional.of(certificate));

        GiftCertificate expected = GiftCertificate.builder()
                .name("image")
                .description("new description")
                .price(new BigDecimal("40"))
                .duration(20)
                .tags(tags)
                .category(Category.builder().name("Food").build())
                .image(IMAGE_FILE_NAME)
                .createDate(LocalDateTime.now(clock))
                .lastUpdateDate(LocalDateTime.now(clock).truncatedTo(ChronoUnit.MILLIS))
                .build();
        GiftCertificate actual = giftCertificateService
                .updateGiftCertificate(1, newCertificate, Optional.of(new FileImageDto(null, ".jpg")));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testUpdateGiftCertificateWithNotValidField() {
        GiftCertificate certificate = GiftCertificate.builder()
                .name("Ali-ba-ba")
                .price(new BigDecimal("40"))
                .description("new description")
                .duration(20)
                .tags(tags)
                .image(IMAGE_FILE_NAME)
                .category(Category.builder().name("food").build())
                .build();
        GiftCertificate newCertificate = GiftCertificate.builder()
                .name("  ")
                .description("  ")
                .price(null)
                .duration(0)
                .category(null)
                .tags(Set.of())
                .build();

        when(giftCertificateDao.findById(anyInt())).thenReturn(Optional.of(certificate));

        GiftCertificate actual = giftCertificateService
                .updateGiftCertificate(1, newCertificate, Optional.empty());
        GiftCertificate expected = GiftCertificate.builder()
                .name("Ali-ba-ba")
                .description("new description")
                .price(new BigDecimal("40"))
                .duration(20)
                .image(IMAGE_FILE_NAME)
                .category(Category.builder().name("food").build())
                .tags(Set.of())
                .lastUpdateDate(LocalDateTime.now(clock).truncatedTo(ChronoUnit.MILLIS))
                .build();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testUpdateNotExistGiftCertificate() {
        GiftCertificate certificate = certificateList.get(0);
        when(giftCertificateDao.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> giftCertificateService.updateGiftCertificate(1, certificate, null));
    }

    @Test
    void testDeleteCertificate() {
        when(giftCertificateDao.findById(anyInt()))
                .thenReturn(Optional.of(GiftCertificate.builder().image("test/001.jpg").build()));

        giftCertificateService.deleteCertificate(2);

        verify(imageService).deleteImage("test/001.jpg");
        verify(giftCertificateDao).deleteById(2);
    }

    @Test
    void testFindCertificateImage() {
        when(giftCertificateDao.findById(2))
                .thenReturn(Optional.of(GiftCertificate.builder().image("test/001.jpg").build()));
        when(imageService.getImage("test/001.jpg")).thenReturn(Optional.of(new byte[] {0, 1, 2}));

        byte[] expected = {0, 1, 2};
        Optional<byte[]> actual = giftCertificateService.findCertificateImage(2);

        assertThat(actual).contains(expected);
    }

    @Test
    void testDeleteNonCertificate() {
        when(giftCertificateDao.findById(anyInt()))
                .thenReturn(Optional.empty());

        giftCertificateService.deleteCertificate(2);

        verify(imageService, never()).deleteImage(anyString());
        verify(giftCertificateDao).deleteById(2);
    }
}