package entreprisecorp.restservices.controllers;

import java.util.concurrent.atomic.AtomicLong;

import com.google.gson.Gson;
import entreprisecorp.App;
import entreprisecorp.restservices.ResponseSuccess;
import entreprisecorp.restservices.models.ApiKey;
import entreprisecorp.restservices.models.admin.AdminRepository;
import entreprisecorp.restservices.models.features.FeatureRepository;
import entreprisecorp.restservices.models.features.MatchFeatures;
import entreprisecorp.restservices.models.features.MatchWithApiKey;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import entreprisecorp.App;
import entreprisecorp.restservices.ResponseSuccess;
import entreprisecorp.restservices.models.features.MatchFeatures;
import entreprisecorp.restservices.models.features.MatchWithApiKey;

@RestController
public class ELOController {
    private final AtomicLong counter = new AtomicLong();

    private final AdminRepository repository;
    private final FeatureRepository repositoryFeature;

    public ELOController(AdminRepository repository, FeatureRepository featureRepository) {
        this.repository = repository;
        this.repositoryFeature = featureRepository;
    }


    @PostMapping(
            path = "/returnResultMatch",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseSuccess returnResultMatch(@RequestBody MatchWithApiKey matchWithApiKey){

        //String tableNameFromApiKey = repository.findByApiKey(matchWithApiKey.getApiKey()).getTableFeatures();
        MatchFeatures matchFeatures = App.eloHandler.calculateNewEloMatch(matchWithApiKey.getMatchFeatures());

        try{
            repositoryFeature.save(matchFeatures.getFeature2());
            repositoryFeature.save(matchFeatures.getFeature1());
            System.err.println("Feature Updates done!");
            Gson gson = new Gson();
            String featureJson = gson.toJson(matchFeatures);
            return new ResponseSuccess(counter.incrementAndGet(), featureJson, true);
        }
        catch (Exception e){
            System.err.println("Feature Updates failed!");
            return new ResponseSuccess(counter.incrementAndGet(), "", false);
        }


    }

}
