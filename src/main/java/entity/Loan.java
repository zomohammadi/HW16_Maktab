package entity;

import enumaration.Degree;
import enumaration.LoanType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
@Entity

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Loan extends BaseEntity {
    public static final String LOAN_TYPE = "loan_type";
    public static final String AMOUNT = "amount";
    public static final String DEGREE = "degree";

    @Column(name = LOAN_TYPE)
    @Enumerated(EnumType.STRING)
    LoanType loanType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    Term term;

    @Column(name = AMOUNT)
    Double amount;

    @Column(name = DEGREE)
    @Enumerated(EnumType.STRING)
    Degree degree;

}
