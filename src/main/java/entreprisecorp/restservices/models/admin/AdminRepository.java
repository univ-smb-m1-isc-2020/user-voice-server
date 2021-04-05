package entreprisecorp.restservices.models.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long>{
    Admin findByEmailAndPassword(String email, String password);

    Admin findByApiKey(String apiKey);

    Admin findByEmail(String email);

}
