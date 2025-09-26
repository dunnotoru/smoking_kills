package dunno.smoking_kills.item;

public class CigarettePackSettings {
    public int strength = 1;
    public String flavor = "Tobacco";

    public CigarettePackSettings strength(int strength) {
        this.strength = strength;
        return this;
    }

    public CigarettePackSettings flavor(String flavor) {
        this.flavor = flavor;
        return this;
    }
}