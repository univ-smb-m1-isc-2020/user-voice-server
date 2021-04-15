package entreprisecorp.restservices.models.features;

import entreprisecorp.restservices.models.apikeys.WebSite;

import javax.persistence.*;

@Entity
public class Feature {

    @Id
    @GeneratedValue
    private Long id;

    private String text;

    private String emailAuthor;

    private int ELO;


    @Transient
    private boolean won;

    @ManyToOne
    private WebSite webSite;

    public Feature() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEmailAuthor() {
        return emailAuthor;
    }

    public void setEmailAuthor(String emailAuthor) {
        this.emailAuthor = emailAuthor;
    }

    public int getELO() {
        return ELO;
    }

    public void setELO(int ELO) {
        this.ELO = ELO;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public WebSite getWebSite() {
        return webSite;
    }

    public void setWebSite(WebSite webSite) {
        this.webSite = webSite;
    }

    @Override
    public String toString() {
        return "Feature{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", emailAuthor='" + emailAuthor + '\'' +
                ", ELO=" + ELO +
                ", won=" + won +
                ", webSite=" + webSite +
                '}';
    }
}
