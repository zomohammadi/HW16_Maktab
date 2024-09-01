package entity;

import jakarta.persistence.*;
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
public class Payment extends BaseEntity {
    public static final String INSTALLMENT_NUMBER = "installment_number";
    public static final String AMOUNT_PER_INSTALLMENT = "amount_per_installment";
    public static final String PREPAYMENT_DATE = "prepayment_date";
    private static final String IS_PAYED = "is_payed";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    Loan loan;

    @Column(name = INSTALLMENT_NUMBER)
    int installmentNumber;

    @Column(name = AMOUNT_PER_INSTALLMENT)
    Double amountPerInstallment;

    @Column(name = PREPAYMENT_DATE)
    ZonedDateTime prepaymentDate;

    @Column(name = IS_PAYED)
    boolean isPayed;
}
