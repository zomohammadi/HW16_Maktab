package repository;

import entity.Student;
import jakarta.persistence.Tuple;

import java.util.List;

public interface PaymentRepository {
    List<jakarta.persistence.Tuple> showPaidInstallments(Student student);
    List<Tuple> showUnPaidInstallments(Student student);
    List<Tuple> listOfLoanThatMustBePayed(Long id);
}
