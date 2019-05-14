package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.models;

/**
 * Created by Tonatiuh on 04/10/2017.
 */
public class ItemSlideMainMenu {
    private int imgId;
    private String title;

    public ItemSlideMainMenu(int imgId, String title) {
        this.imgId = imgId;
        this.title = title;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
