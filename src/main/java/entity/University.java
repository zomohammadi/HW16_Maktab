package entity;

import enumaration.TypeOfUniversity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Check;

@SuperBuilder
@Entity

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
//@Check(constraints = "LOAN_TYPE = Education OR LOAN_TYPE = TuitionFee")

public class University extends BaseEntity {
    public static final String NAME = "name";
    public static final String TYPE_OF_UNIVERSITY = "type_of_university";

    @Column(name = NAME,unique = true)
    String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    City city;

    @Column(name = TYPE_OF_UNIVERSITY)
    @Enumerated(EnumType.STRING)
    TypeOfUniversity typeOfUniversity;
}
