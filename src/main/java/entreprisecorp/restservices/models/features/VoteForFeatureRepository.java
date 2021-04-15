package entreprisecorp.restservices.models.features;

import entreprisecorp.restservices.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface VoteForFeatureRepository extends JpaRepository<VoteForFeature,Long> {

    ArrayList<VoteForFeature> findAllByUser(User user);
}
