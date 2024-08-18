package entity;

import enumaration.LoanType;
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
public class Loan extends BaseEntity {
    public static final String LOAN_TYPE = "loan_type";
    public static final String YEAR = "year";

    @Column(name = LOAN_TYPE)
    LoanType loanType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    Student student;

    @Column(name = YEAR)
    Integer year;
}
