package at.ac.tuwien.sepm.ws16.qse01.entities;

/**
 * Logo-RelativeRectangle Pair Entity
 */
public class PairLogoRelativeRectangle {
    private Logo logo;
    private RelativeRectangle relativeRectangle;

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public PairLogoRelativeRectangle(Logo logo, RelativeRectangle relativeRectangle) {
        this.logo = logo;
        this.relativeRectangle = relativeRectangle;
    }

    public RelativeRectangle getRelativeRectangle() {
        return relativeRectangle;
    }

    public void setRelativeRectangle(RelativeRectangle relativeRectangle) {
        this.relativeRectangle = relativeRectangle;
    }
}
