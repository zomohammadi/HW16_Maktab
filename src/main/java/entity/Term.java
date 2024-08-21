package entity;

import enumaration.TermType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Term extends BaseEntity {
    public static final String TERM_TYPE = "term_type";
    public static final String YEAR = "year";

    @Enumerated(EnumType.STRING)
    @Column(name = TERM_TYPE)
    TermType termType;

    @Column(name = YEAR)
    Integer year;
}
