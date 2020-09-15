package ma.forix.gui;

import ma.forix.assets.Assets;
import ma.forix.renderer.Camera;
import ma.forix.renderer.Shader;
import ma.forix.renderer.TileSheet;
import ma.forix.util.Input;
import org.joml.Matrix4f;

public class Text implements Widget {

    private Matrix4f transform = new Matrix4f();
    private TileSheet sheet;
    private String text = "ab";

    public Text(TileSheet sheet, String text){
        this.sheet = sheet;
        this.text = text;
    }

    public void setText(String text){
        this.text = text;
    }

    @Override
    public void render(Camera camera, Shader shader) {
        shader.bind();
        int x = -600;
        for (char c : text.toCharArray()){
            byte code = (byte) ((byte)c);
            sheet.bindTile(shader, code);
            transform.identity().translate(x, 10, 0).scale(20, 20, 1);
            shader.setUniform("projection", camera.getProjection().mul(transform));
            Assets.getModel().render();


            /*switch (c){
                case 'a':
                    sheet.bindTile(shader, 0, 0);
                    transform.identity().translate(x, 10, 0).scale(20, 20, 1);
                    shader.setUniform("projection", camera.getProjection().mul(transform));
                    Assets.getModel().render();
                    break;
                case 'b':
                    sheet.bindTile(shader, 1, 0);
                    transform.identity().translate(x, 10, 0).scale(20, 20, 1);
                    shader.setUniform("projection", camera.getProjection().mul(transform));
                    Assets.getModel().render();
                    break;
                case 'c':
                    sheet.bindTile(shader, 2, 0);
                    transform.identity().translate(x, 10, 0).scale(20, 20, 1);
                    shader.setUniform("projection", camera.getProjection().mul(transform));
                    Assets.getModel().render();
                    break;
                case 'd':
                    sheet.bindTile(shader, 3, 0);
                    transform.identity().translate(x, 10, 0).scale(20, 20, 1);
                    shader.setUniform("projection", camera.getProjection().mul(transform));
                    Assets.getModel().render();
                    break;
                case 'e':
                    sheet.bindTile(shader, 4, 0);
                    transform.identity().translate(x, 10, 0).scale(20, 20, 1);
                    shader.setUniform("projection", camera.getProjection().mul(transform));
                    Assets.getModel().render();
                    break;
            }*/
            x+=32;
        }

    }

    @Override
    public void update(Input input) {

    }
}
