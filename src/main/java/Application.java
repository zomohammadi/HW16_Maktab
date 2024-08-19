import jakarta.persistence.EntityManager;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import util.ApplicationContext;

public class Application {
    public static void main(String[] args) {
        ApplicationContext.getInstance().getMainMenu().showMainMenu();


   /*   //handle student exceptions in student menu

     Student student = new Student();
        // student.setName("John Doe");
        // student.setId(1L);
        // Set other student properties here

        try {
            // Attempt to save the student
            studentService.save(student);
            System.out.println("Student saved successfully!");

        } catch (StudentExceptions.StudentNullPointerException e) {
            System.out.println("Error: The student object is null. Please provide a valid student.");
        } catch (StudentExceptions.StudentIllegalArgumentException e) {
            System.out.println("Error: Invalid argument provided: " + e.getMessage());
        } catch (StudentExceptions.StudentEntityExistsException e) {
            System.out.println("Error: The student already exists in the database.");
        } catch (StudentExceptions.StudentTransactionRequiredException e) {
            System.out.println("Error: A transaction is required but was not active.");
        } catch (StudentExceptions.StudentPersistenceException e) {
            System.out.println("Error: A persistence error occurred: " + e.getMessage());
        } catch (Exception e) {
            // Catch-all for any other unexpected exceptions
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }*/

        // Continue with the rest of your application logic
    }
}
