import jakarta.persistence.EntityManager;
import util.ApplicationContext;

public class Application {
    public static void main(String[] args) {
         ApplicationContext.getInstance().getEntityManager();
    }
}
