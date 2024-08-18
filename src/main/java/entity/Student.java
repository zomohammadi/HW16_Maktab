package entity;

import enumaration.AdmissionType;
import enumaration.Degree;
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

public class Student extends BaseEntity {
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String FATHER_NAME = "father_name";
    public static final String MATHER_NAME = "mather_name";
    public static final String BIRTH_CERTIFICATE_NUMBER = "birth_certificate_number";
    public static final String NATIONAL_CODE = "national_code";
    public static final String STUDENT_CODE = "student_code";
    public static final String ENTRY_YEAR = "entry_year";
    public static final String BIRTHDATE = "birthdate";
    public static final String DEGREE = "degree";
    public static final String IS_MARRIED = "is_married";
    public static final String PARTNER_CODE = "partner_code";
    public static final String ADMISSION_TYPE = "admission_type";

    @Column(name = FIRST_NAME)

    String firstName;

    @Column(name = LAST_NAME)
    String lastName;

    @Column(name = FATHER_NAME)
    String fatherName;

    @Column(name = MATHER_NAME)
    String MatherName;

    @Column(name = BIRTH_CERTIFICATE_NUMBER)
    String birthCertificateNumber;

    @Column(name = NATIONAL_CODE)
    String nationalCode;

    @Column(name = STUDENT_CODE)
    String studentCode;

    @Column(name = ENTRY_YEAR)
    Integer entryYear;

    @Column(name = BIRTHDATE)
    LocalDate birthdate;

    @Column(name = DEGREE)
    Degree degree;

    @Column(name = IS_MARRIED)
    boolean isMarried;

    @Column(name = PARTNER_CODE)
    String partnerCode;

    @Column(name = ADMISSION_TYPE)
    AdmissionType admissionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    University university;

}
