package entreprisecorp.restservices.models.features;

import entreprisecorp.restservices.models.apikeys.WebSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public interface FeatureRepository extends JpaRepository<Feature,Long> {

    ArrayList<Feature> findAllByWebSite(WebSite webSite);

    Optional<Feature> findById(Long id);

}
