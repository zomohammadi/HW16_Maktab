package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Table(name = MortgageDetail.MORTGAGE_DETAIL)

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MortgageDetail extends BaseEntity {
    public static final String MORTGAGE_DETAIL = "mortgage_detail";
    public static final String CONTRACT_NUMBER = "contract_number";
    public static final String ADDRESS = "address";

    @OneToOne
    Loan loan;

    @Column(name = CONTRACT_NUMBER)
    String contractNumber;

    @Column(name = ADDRESS)
    String address;
}
