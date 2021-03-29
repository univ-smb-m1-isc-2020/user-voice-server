package entreprisecorp.restservices.controllers;

import com.google.gson.Gson;
import entreprisecorp.App;
import entreprisecorp.restservices.ResponseSuccess;
import entreprisecorp.restservices.models.ApiKey;
import entreprisecorp.restservices.models.admin.Admin;
import entreprisecorp.restservices.models.admin.AdminRepository;
import entreprisecorp.restservices.models.features.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class FeaturesController {
    private final AtomicLong counter = new AtomicLong();

    private final AdminRepository repository;
    private final FeatureRepository repositoryFeature;

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

        String tableNameFromApiKey = App.adminDbHandler.getTableNameFromApiKey(apikey.getApiKey());
        return App.featuresDbHandler.getMatchFeature(tableNameFromApiKey);

    }

    @PostMapping(
            path = "/addFeature",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseSuccess addFeature(@RequestBody FeatureWithApiKey featureWithApiKey){


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

        ListFeatures listFeatures = App.featuresDbHandler.getFeatureByAuthor(featureAndTableName.getEmail(),featureAndTableName.getTableName());
        return listFeatures;
    }

    @PostMapping(
            path = "/getFeatureByTable",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ListFeatures getFeaturesTable(@RequestBody FeatureAndTableName featureAndTableName){

        ListFeatures listFeatures = App.featuresDbHandler.getFeatureByTable(featureAndTableName.getTableName());
        return listFeatures;
    }


}
