package ma.forix.renderer;

import org.joml.Matrix4f;

public class TileSheet {
    private Texture texture;

    private Matrix4f scale;
    private Matrix4f translation;

    private int tilesAmount;

    public TileSheet(String texture, int tilesAmount){
        this.texture = new Texture("gui/"+texture);

        scale = new Matrix4f().scale(1.0f/(float)tilesAmount);
        translation = new Matrix4f();

        this.tilesAmount = tilesAmount;
    }

    public TileSheet(String texture){
        this(texture, 1);
    }

    public void bindTile(Shader shader, int x, int y){
        scale.translate(x, y, 0, translation);

        shader.setUniform("sampler", 0);
        shader.setUniform("texModifier", translation);
        texture.bind(0);
    }

    public void bindTile(Shader shader, int tile){
        int x = tile % tilesAmount;
        int y = tile / tilesAmount;
        bindTile(shader, x, y);
    }

    public Texture getTexture() {
        return texture;
    }
}
