package entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Table(name = LoanCreditCard.TABLE_NAME)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoanCreditCard extends BaseEntity {
public static final String TABLE_NAME = "loan_creditCard";
    @OneToOne
    Loan loan;

    @ManyToOne
    @JoinColumn
    CreditCard creditCard;

}
