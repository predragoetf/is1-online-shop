package perzistentne_klase;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import perzistentne_klase.KorpaB;
import perzistentne_klase.ProdavacB;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-02-11T01:24:11")
@StaticMetamodel(ArtikalB.class)
public class ArtikalB_ { 

    public static volatile SingularAttribute<ArtikalB, ProdavacB> prodavac;
    public static volatile SingularAttribute<ArtikalB, Integer> idArtikla;
    public static volatile SingularAttribute<ArtikalB, Integer> kolicina;
    public static volatile SingularAttribute<ArtikalB, Integer> tip;
    public static volatile SingularAttribute<ArtikalB, Integer> cena;
    public static volatile SingularAttribute<ArtikalB, KorpaB> korpa;

}