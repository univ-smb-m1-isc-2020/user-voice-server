package entreprisecorp.restservices.models.features;

import org.springframework.test.annotation.IfProfileValue;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name="Feature.getTwoRandom",
                query="select f from Feature f")
})
@Entity
public class Feature {

    @Id
    @GeneratedValue
    private int id;
    private String textFeature;
    private int ELO = 0;
    private String authorEmail;
    private boolean won;
    private String tableName;

    public Feature(int id, String textFeature, int ELO,String authorEmail) {
        this.id = id;
        this.textFeature = textFeature;
        this.ELO = ELO;
        this.authorEmail = authorEmail;
    }



    public Feature(String textFeature, int ELO,String authorEmail) {
        this.textFeature = textFeature;
        this.ELO = ELO;
        this.authorEmail = authorEmail;
    }

    public Feature(String textFeature, String authorEmail) {
        this.textFeature = textFeature;
        this.authorEmail = authorEmail;
    }

    public Feature() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTextFeature() {
        return textFeature;
    }

    public void setTextFeature(String textFeature) {
        this.textFeature = textFeature;
    }

    public int getELO() {
        return ELO;
    }

    public void setELO(int ELO) {
        this.ELO = ELO;
    }



    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "Feature{" +
                "id=" + id +
                ", textFeature='" + textFeature + '\'' +
                ", ELO=" + ELO +
                ", authorEmail='" + authorEmail + '\'' +
                '}';
    }
}
