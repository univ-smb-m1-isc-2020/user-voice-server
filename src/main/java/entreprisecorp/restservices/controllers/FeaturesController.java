package entreprisecorp.restservices.controllers;

import com.google.gson.Gson;
import entreprisecorp.restservices.ResponseSuccess;
import entreprisecorp.restservices.models.ApiKey;
import entreprisecorp.restservices.models.admin.Admin;
import entreprisecorp.restservices.models.admin.AdminRepository;
import entreprisecorp.restservices.models.features.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class FeaturesController {
    private final AtomicLong counter = new AtomicLong();

    private final AdminRepository repository;
    private final FeatureRepository repositoryFeature;
    private Random rand = new SecureRandom();

    public FeaturesController(AdminRepository repository, FeatureRepository featureRepository) {
        this.repository = repository;
        this.repositoryFeature = featureRepository;
    }


    @PostMapping(
            path = "/getMatch",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public MatchFeatures getMatchFeature(@RequestBody ApiKey apikey){


        try{
            String tableNameFromApiKey = repository.findByApiKey(apikey.getApiKey()).getTableFeatures();
            List<Feature> features = repositoryFeature.findAllByTableName(tableNameFromApiKey);

            Feature feature1 = features.get(rand.nextInt(features.size()));
            Feature feature2;
            do{
                feature2 = features.get(rand.nextInt(features.size()));
            }
            while(feature1 == feature2);


            return new MatchFeatures(feature1,feature2);

        }
        catch (Exception e){
            return new MatchFeatures(null,null);
        }

    }

    @PostMapping(
            path = "/addFeature",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseSuccess addFeature(@RequestBody FeatureWithApiKey featureWithApiKey){
        featureWithApiKey.getFeature().setELO(1000);

        try{
            Admin admin = repository.findByApiKey(featureWithApiKey.getApiKey());
            featureWithApiKey.getFeature().setTableName(admin.getTableFeatures());
            repositoryFeature.saveAndFlush(featureWithApiKey.getFeature());
            System.err.println("Feature Creation done!");
            Gson gson = new Gson();
            String featureJson = gson.toJson(featureWithApiKey.getFeature());
            return new ResponseSuccess(counter.incrementAndGet(), featureJson, true);

        }
        catch (Exception exception){
            System.err.println("Feature Creation failed!");
            return new ResponseSuccess(counter.incrementAndGet(), "", false);
        }

    }

    @PostMapping(
            path = "/getFeatureByAuthor",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ListFeatures getFeaturesAuthor(@RequestBody FeatureAndTableName featureAndTableName){

        ListFeatures listFeatures = new ListFeatures(repositoryFeature.findAllByAuthorEmailAndTableName(featureAndTableName.getEmail(),featureAndTableName.getTableName()));
        return listFeatures;
    }

    @PostMapping(
            path = "/getFeatureByTable",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ListFeatures getFeaturesTable(@RequestBody FeatureAndTableName featureAndTableName){

        ListFeatures listFeatures = new ListFeatures(repositoryFeature.findAllByTableName(featureAndTableName.getTableName()));
        return listFeatures;
    }


}
