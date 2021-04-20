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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private VoteForFeatureRepository voteForFeatureRepository;

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


    @PostMapping(
            path = "/features/voteForFeature",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE    )
    public ResponseSuccess voteForFeature(@RequestBody Feature feature){

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principal.getUser();

        try{
            LocalDate date = LocalDate.now();
            LocalDate dateYesterday = date.minusDays(1);
            int numberVoteToday = 0;
            boolean isAlreadyVoted = false;
            String json = "";

            ArrayList<VoteForFeature> voteForFeatures = voteForFeatureRepository.findAllByUser(user);


            for (VoteForFeature voteForFeature: voteForFeatures) {
                if(voteForFeature.getDate().isAfter(dateYesterday)){
                    numberVoteToday++;
                    if(voteForFeature.getFeature().getId().equals(feature.getId())){
                        isAlreadyVoted = true;
                    }
                }
            }
            if(numberVoteToday < 10 && !isAlreadyVoted){
                feature.setELO(feature.getELO() + 20);
                featureRepository.save(feature);

                VoteForFeature voteForFeature = new VoteForFeature(feature,user,date);
                voteForFeatureRepository.saveAndFlush(voteForFeature);
                json = voteForFeature.toString();

                System.err.println("Feature Updates done! " + numberVoteToday);
            }
            else if(isAlreadyVoted){
                json = "AlreadyVoted";
            }
            else{
                json = "Too much vote";
            }
            return new ResponseSuccess(counter.incrementAndGet(), json, true);
        }
        catch (Exception e){
            System.err.println("Feature Updates failed!");
            return new ResponseSuccess(counter.incrementAndGet(), "too much vote", false);
        }


    }

    @PostMapping( path = "/features/getNumberVoteToday")
    public ResponseSuccess getNumberVoteToday(){

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principal.getUser();

        try{
            LocalDate date = LocalDate.now();
            LocalDate dateYesterday = date.minusDays(1);
            int numberVoteToday = 0;
            ArrayList<VoteForFeature> voteForFeatures = voteForFeatureRepository.findAllByUser(user);
            for (VoteForFeature voteForFeature: voteForFeatures) {
                if(voteForFeature.getDate().isAfter(dateYesterday)){
                    numberVoteToday++;
                }
            }
            return new ResponseSuccess(counter.incrementAndGet(), String.valueOf(numberVoteToday), true);
        }
        catch (Exception e){
            System.err.println("Cant get result!");
            return new ResponseSuccess(counter.incrementAndGet(), "error getting result", false);
        }


    }




}
