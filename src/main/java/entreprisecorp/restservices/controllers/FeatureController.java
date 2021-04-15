package entreprisecorp.restservices.controllers;

import com.google.gson.Gson;
import entreprisecorp.App;
import entreprisecorp.restservices.ResponseSuccess;
import entreprisecorp.restservices.models.apikeys.ApiKey;
import entreprisecorp.restservices.models.apikeys.WebSite;
import entreprisecorp.restservices.models.apikeys.WebSiteRepository;
import entreprisecorp.restservices.models.features.*;
import entreprisecorp.restservices.models.user.User;
import entreprisecorp.restservices.models.user.UserPrincipal;
import entreprisecorp.utils.ELOHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class FeatureController {
    private final AtomicLong counter = new AtomicLong();
    private Random rand = new SecureRandom();

    @Autowired
    private WebSiteRepository webSiteRepository;

    @Autowired
    private FeatureRepository featureRepository;

    public ELOHandler eloHandler = new ELOHandler();


    @PostMapping(
            path = "/features/create",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseSuccess createFeature(@RequestBody FeatureWithApiKey featureWithApiKey){


        Feature feature = featureWithApiKey.getFeature();
        feature.setELO(1000);
        feature.setWebSite(webSiteRepository.findByApiKey(featureWithApiKey.getApiKey()));

        try{
            featureRepository.saveAndFlush(feature);
            return new ResponseSuccess(counter.incrementAndGet(), feature.toString(), true);
        }
        catch (Exception exception){
            return new ResponseSuccess(counter.incrementAndGet(), "register feature failed", false);
        }


    }

    @PostMapping(
            path = "/features/getAllFromWebSite",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseSuccess GetAllFeatureWebsite(@RequestBody ApiKey apiKey){
        try{
            WebSite webSite = webSiteRepository.findByApiKey(apiKey.getApiKey());
            ArrayList<Feature> features = featureRepository.findAllByWebSite(webSite);
            return new ResponseSuccess(counter.incrementAndGet(), features.toString(), true);
        }
        catch (Exception exception){
            return new ResponseSuccess(counter.incrementAndGet(), "no features", false);
        }
    }

    @PostMapping(
            path = "/features/getMatch",
            consumes = "application/json",
            produces = "application/json"
    )
    public MatchFeatures GetMatch(@RequestBody ApiKey apiKey){
        try{
            WebSite webSite = webSiteRepository.findByApiKey(apiKey.getApiKey());
            ArrayList<Feature> features = featureRepository.findAllByWebSite(webSite);

            Feature feature1 = features.get(rand.nextInt(features.size()));
            Feature feature2;
            do{
                feature2 = features.get(rand.nextInt(features.size()));
            }
            while(feature1 == feature2);

            return new MatchFeatures(feature1,feature2);
        }
        catch (Exception exception){
            return null;
        }
    }

    @PostMapping(
            path = "/features/returnResultMatch",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseSuccess returnResultMatch(@RequestBody MatchWithApiKey matchWithApiKey){

        MatchFeatures matchFeatures = eloHandler.calculateNewEloMatch(matchWithApiKey.getMatchFeatures());

        try{
            featureRepository.save(matchFeatures.getFeature2());
            featureRepository.save(matchFeatures.getFeature1());
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
