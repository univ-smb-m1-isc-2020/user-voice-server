package entreprisecorp.restservices.models.features;

import entreprisecorp.restservices.models.apikeys.WebSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Service
public interface FeatureRepository extends JpaRepository<Feature,Long> {

    ArrayList<Feature> findAllByWebSite(WebSite webSite);

}
