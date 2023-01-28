package com.epam.esm.entity;

import com.epam.esm.listener.AuditListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tags", schema = "module_7")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
@EntityListeners(AuditListener.class)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int tagId;

    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private List<GiftCertificate> giftCertificates = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Tag " + tagId + " " + name;
    }
}
