package entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Entity

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreditCard extends BaseEntity {
    public static final String ACCOUNT = "account";
    public static final String CARD_NUMBER = "card_number";
    public static final String CVV2 = "cvv2";
    public static final String EXPIRATION_DATE = "expiration_date";

    @OneToOne
    @PrimaryKeyJoinColumn
    Account account;

    @Column(name = CARD_NUMBER)
    String cardNumber;

    @Column(name = CVV2)
    String cvv2;

    @Column(name = EXPIRATION_DATE)
    LocalDate expirationDate;
}
