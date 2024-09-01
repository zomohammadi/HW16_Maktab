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
    public static final String ACCOUNT_NUMBER = "account_number";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    Bank bank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    Student student;

    @Column(name = BALANCE)
    double balance;

    @Column(name = ACCOUNT_NUMBER, unique = true)
    int accountNumber;
}
