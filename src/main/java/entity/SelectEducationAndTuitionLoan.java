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
public class SelectEducationAndTuitionLoan extends BaseEntity {
    public static final String LOAN_TYPE = "loan_type";
    public static final String AMOUNT = "amount";
    public static final String FIRST_SEMESTER = "first_semester";
    public static final String SECOND_SEMESTER = "second_semester";
    public static final String DEGREE = "degree";
    public static final String YEAR = "year";

    @ManyToOne
    @JoinColumn
    Student student;

    @Column(name = LOAN_TYPE)
    @Enumerated(EnumType.STRING)
    LoanType loanType;

    @Column(name = AMOUNT)
    Double amount;

    @Column(name = FIRST_SEMESTER)
    boolean firstSemester;

    @Column(name = SECOND_SEMESTER)
    boolean secondSemester;

    @Column(name = DEGREE)
    Degree degree;

    @Column(name = YEAR)
    Integer year;

}
