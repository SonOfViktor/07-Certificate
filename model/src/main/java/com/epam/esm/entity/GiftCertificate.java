package com.epam.esm.entity;

import com.epam.esm.listener.AuditListener;
import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "gift_certificates", schema = "module_7")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
@EntityListeners(AuditListener.class)
public class GiftCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int giftCertificateId;

    private String name;

    private String description;

    private BigDecimal price;

    private int duration;

    private LocalDateTime createDate;

    private LocalDateTime lastUpdateDate;

    private String image;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "gift_certificate_tag", schema = "module_7",
            joinColumns = @JoinColumn(name = "gift_certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "giftCertificate")
    private List<UserOrder> userOrders;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate.truncatedTo(ChronoUnit.MILLIS);
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate.truncatedTo(ChronoUnit.MILLIS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificate that = (GiftCertificate) o;
        return duration == that.duration &&
                Objects.equals(name, that.name) && Objects.equals(description, that.description) &&
                Objects.equals(price, that.price) && Objects.equals(createDate, that.createDate) &&
                Objects.equals(lastUpdateDate, that.lastUpdateDate) &&
                Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, duration, createDate, lastUpdateDate, image);
    }

    @Override
    public String toString() {
        return  "\nGiftCertificate #" + giftCertificateId +
                " " + name + "\n" +
                "description " + description + "\n" +
                "duration " + duration + " day(s)" +
                ", price " + price +
                ", createDate " + createDate +
                ", lastUpdateDate " + lastUpdateDate +
                ", image " + image;
    }
}
