package entreprisecorp.restservices.models.features;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface FeatureRepository extends JpaRepository<Feature, Long> {
    ArrayList<Feature> findAllByAuthorEmailAndTableName(String email, String tableName);

    ArrayList<Feature> findAllByTableName(String tableName);
}
