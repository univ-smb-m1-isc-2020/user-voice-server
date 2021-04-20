package entreprisecorp.restservices.models.features;

import entreprisecorp.restservices.models.user.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class VoteForFeature {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Feature feature;

    @ManyToOne
    private User user;

    private LocalDate date;

    public VoteForFeature() {
    }

    public VoteForFeature(Feature feature, User user, LocalDate date) {
        this.feature = feature;
        this.user = user;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", feature=" + feature +
                ", user=" + user +
                ", date=" + date +
                '}';
    }


}
