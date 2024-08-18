package entity;

import jakarta.persistence.*;
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
public class Account extends BaseEntity {
    public static final String BALANCE = "balance";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    Bank bank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    Student student;

    @Column(name = BALANCE)
    Double balance;
}
