package entreprisecorp.restservices.models.apikeys;

import entreprisecorp.restservices.models.user.User;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class WebSite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nameSite;
    private String apiKey;

    @ManyToOne
    private User owner;

    public WebSite() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameSite() {
        return nameSite;
    }

    public void setNameSite(String nameSite) {
        this.nameSite = nameSite;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    @Override
    public String toString() {
        return "WebSite{" +
                "id=" + id +
                ", nameSite='" + nameSite + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", user=" + owner +
                '}';
    }

    public String generateAPIKEY(){
        UUID uuid = UUID.randomUUID();
        String apikey = uuid.toString();
        return apikey;
    }

}
