package ma.forix.gui;

import ma.forix.assets.Assets;
import ma.forix.item.Item;
import ma.forix.renderer.Camera;
import ma.forix.renderer.Shader;
import ma.forix.renderer.Texture;
import org.joml.Matrix4f;

public class TextureRenderer {

    private Shader shader;
    private Matrix4f transform;
    private Camera camera;

    public TextureRenderer(Camera camera, Shader shader){
        this.camera = camera;
        this.shader = shader;
    }

    public void renderTexture(Texture texture, int x, int y, int width, int height){
        transform = new Matrix4f().identity().translate(x, y, 0).scaleXY(texture.getWidth()*width, texture.getHeight()*height);
        shader.bind();
        texture.bind(0);
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        Assets.getModel().render();
    }

    public void renderTexture(Texture texture, int x, int y){
        renderTexture(texture, x, y, 1, 1);
    }

    public void renderTexture(Texture texture, int x, int y, int scale){
        renderTexture(texture, x, y, scale, scale);
    }

    public void renderItem(Item item, int x, int y){
        Texture texture = new Texture(item.getTexture()+".png");
        renderTexture(texture, x, y, 1, 1);
    }
}
