package entity;

import enumaration.TypeOfCity;
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
public class City extends BaseEntity {
    public static final String NAME = "name";
    public static final String TYPE_OF_CITY = "type_Of_city";
    @Column(name = NAME,unique = true)
    String name;

    @Column(name = TYPE_OF_CITY)
    @Enumerated(EnumType.STRING)
    TypeOfCity typeOfCity;


}
