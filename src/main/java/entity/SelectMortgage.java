package entity;

import enumaration.Degree;
import enumaration.LoanType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity

@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class SelectMortgage extends BaseEntity {
    public static final String LOAN_TYPE = "loan_type";
    public static final String AMOUNT = "amount";
    public static final String DEGREE = "degree";


    @ManyToOne
    @JoinColumn
    Student student;

    @Column(name = LOAN_TYPE)
    @Enumerated(EnumType.STRING)
    LoanType loanType;

    @Column(name = AMOUNT)
    Double amount;

    @Column(name = DEGREE)
    Degree degree;
}
