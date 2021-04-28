package entreprisecorp.restservices.models.apikeys;

import entreprisecorp.restservices.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public interface WebSiteRepository extends JpaRepository<WebSite, Long> {

    Optional<WebSite> findByApiKey(String apiKey);
    ArrayList<WebSite> findAllByOwner(User user);
}
