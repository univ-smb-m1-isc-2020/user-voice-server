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
import java.util.Optional;
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

    Gson gson = new Gson();

    @Autowired
    private VoteForFeatureRepository voteForFeatureRepository;

    public ELOHandler eloHandler = new ELOHandler();


    @PostMapping(
            path = "/features/create",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseSuccess createFeature(@RequestBody FeatureWithApiKey featureWithApiKey){
        Optional<WebSite> webSite = webSiteRepository.findByApiKey(featureWithApiKey.getApiKey());

        if(webSite.isPresent()){
            Feature feature = featureWithApiKey.getFeature();
            feature.setELO(1000);
            feature.setWebSite(webSite.get());

            try{
                featureRepository.saveAndFlush(feature);
                String response = gson.toJson(feature);
                return new ResponseSuccess(counter.incrementAndGet(), response, true);
            }
            catch (Exception exception){
                return new ResponseSuccess(counter.incrementAndGet(), "register feature failed", false);
            }

        }
        return new ResponseSuccess(counter.incrementAndGet(), "wrong api key", false);


    }

    @PostMapping(
            path = "/features/getAllFromWebSite",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseSuccess GetAllFeatureWebsite(@RequestBody ApiKey apiKey){
        try{
            Optional<WebSite> webSite = webSiteRepository.findByApiKey(apiKey.getApiKey());
            if(webSite.isPresent()){
                ArrayList<Feature> features = featureRepository.findAllByWebSite(webSite.get());
                String response = gson.toJson(features);
                return new ResponseSuccess(counter.incrementAndGet(), response, true);

            }
            else{
                return new ResponseSuccess(counter.incrementAndGet(), "Wrong API KEY", false);
            }
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
    public String GetMatch(@RequestBody ApiKey apiKey){
        try{
            Optional<WebSite> webSite = webSiteRepository.findByApiKey(apiKey.getApiKey());
            if(webSite.isPresent()){
                ArrayList<Feature> features = featureRepository.findAllByWebSite(webSite.get());
                if(features.size() < 2){
                    return "not enough features";
                }
                Feature feature1 = features.get(rand.nextInt(features.size()));
                Feature feature2;
                do{
                    feature2 = features.get(rand.nextInt(features.size()));
                }
                while(feature1 == feature2);

                String response = gson.toJson(new MatchFeatures(feature1,feature2));

                return response;

            }
            else{
                return "wrong API KEY";
            }

        }
        catch (Exception exception){
            return "error";
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
    public ResponseSuccess voteForFeature(@RequestBody Feature featureFront){

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principal.getUser();

        Optional<Feature> feature = featureRepository.findById(featureFront.getId());
        if(feature.isPresent()){
            try{
                LocalDate date = LocalDate.now();
                LocalDate dateYesterday = date.minusDays(1);
                int numberVoteToday = 0;
                boolean isAlreadyVoted = false;
                String response = "";
                boolean success = false;

                ArrayList<VoteForFeature> voteForFeatures = voteForFeatureRepository.findAllByUser(user);


                for (VoteForFeature voteForFeature: voteForFeatures) {
                    if(voteForFeature.getDate().isAfter(dateYesterday)){
                        numberVoteToday++;
                        if(voteForFeature.getFeature().getId().equals(feature.get().getId())){
                            isAlreadyVoted = true;
                        }
                    }
                }

                if(numberVoteToday < 10 && !isAlreadyVoted){
                    feature.get().setELO(feature.get().getELO() + 20);
                    featureRepository.save(feature.get());

                    VoteForFeature voteForFeature = new VoteForFeature(feature.get(),user,date);
                    voteForFeatureRepository.saveAndFlush(voteForFeature);

                    response = "Vote succeed";
                    success = true;
                    System.err.println("Feature Updates done! " + numberVoteToday);
                }
                else if(isAlreadyVoted){
                    response = "You have already voted for this feature.";
                }
                else{
                    response = "You have no votes left for today.";
                }
                return new ResponseSuccess(counter.incrementAndGet(), response, success);
            }
            catch (Exception e){
                System.err.println("Feature Updates failed!");
                return new ResponseSuccess(counter.incrementAndGet(), "Unknown issue durin vote", false);
            }

        }
        return new ResponseSuccess(counter.incrementAndGet(), "Unknown issue durin vote", false);


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
            String response = gson.toJson(numberVoteToday);
            return new ResponseSuccess(counter.incrementAndGet(), response, true);
        }
        catch (Exception e){
            System.err.println("Cant get result!");
            return new ResponseSuccess(counter.incrementAndGet(), "error getting result", false);
        }


    }




}
