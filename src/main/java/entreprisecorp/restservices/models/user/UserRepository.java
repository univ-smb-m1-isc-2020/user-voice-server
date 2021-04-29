package entreprisecorp.restservices.models.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select u from User u where u.email=:username or u.username=:username ")
    Optional<User> findByUsername(@Param("username") String username);

    Optional<User> findByEmail(String email);
}
