package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

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
    Account account;

    @Column(name = CARD_NUMBER,unique = true)
    String cardNumber;

    @Column(name = CVV2)
    String cvv2;

    @Column(name = EXPIRATION_DATE)
    ZonedDateTime expirationDate;
}
