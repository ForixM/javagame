package ma.forix.gui;

import ma.forix.assets.Assets;
import ma.forix.collision.AABB;
import ma.forix.collision.Collision;
import ma.forix.renderer.Camera;
import ma.forix.renderer.Shader;
import ma.forix.renderer.TileSheet;
import ma.forix.util.Input;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Button implements Widget {
    public static final int STATE_IDLE = 0;
    public static final int STATE_SELECTED = 1;
    public static final int STATE_CLICKED = 2;
    
    private TileSheet tileSheet;

    private AABB boundingBox;

    private int selectedState;

    private Matrix4f transform = new Matrix4f();

    public Button(Vector2f position, Vector2f scale, TileSheet tileSheet){
        this.boundingBox = new AABB(position, scale);
        this.tileSheet = tileSheet;
    }

    public void update(Input input){
        Collision data = boundingBox.getCollision(input.getMousePosition());

        if (data.isIntersecting) {
            selectedState = STATE_SELECTED;

            if (input.isMouseButtonDown(0)) {
                selectedState = STATE_CLICKED;
            }
        }
        else selectedState = STATE_IDLE;
    }

    @Override
    public void render(Camera camera, Shader shader){
        Vector2f position = boundingBox.getCenter(), scale = boundingBox.getHalfExtent();

        transform.identity().translate(position.x, position.y, 0).scale(scale.x, scale.y, 1); //  MIDDLE/FILL

        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState) {
            case STATE_SELECTED :
                tileSheet.bindTile(shader, 4, 1);
                break;
            case STATE_CLICKED :
                tileSheet.bindTile(shader, 1, 4);
                break;
            default :
                tileSheet.bindTile(shader, 1, 1);
                break;
        }
        Assets.getModel().render();

        renderSides(position, scale, camera, tileSheet, shader);
        renderCorners(position, scale, camera, tileSheet, shader);
    }

    private void renderSides(Vector2f position, Vector2f scale, Camera camera, TileSheet tileSheet, Shader shader){
        transform.identity().translate(position.x, position.y + scale.y - 16, 0).scale(scale.x, 16, 1); //  TOP

        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState) {
            case STATE_SELECTED :
                tileSheet.bindTile(shader, 4, 0);
                break;
            case STATE_CLICKED :
                tileSheet.bindTile(shader, 1, 3);
                break;
            default :
                tileSheet.bindTile(shader, 1, 0);
                break;
        }
        Assets.getModel().render();

        transform.identity().translate(position.x, position.y - scale.y + 16, 0).scale(scale.x, 16, 1); //  BOTTOM

        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState) {
            case STATE_SELECTED :
                tileSheet.bindTile(shader, 4, 2);
                break;
            case STATE_CLICKED :
                tileSheet.bindTile(shader, 1, 5);
                break;
            default :
                tileSheet.bindTile(shader, 1, 2);
                break;
        }
        Assets.getModel().render();

        transform.identity().translate(position.x - scale.x + 16, position.y, 0).scale(16, scale.y, 1); //  LEFT

        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState) {
            case STATE_SELECTED :
                tileSheet.bindTile(shader, 3, 1);
                break;
            case STATE_CLICKED :
                tileSheet.bindTile(shader, 0, 4);
                break;
            default :
                tileSheet.bindTile(shader, 0, 1);
                break;
        }
        Assets.getModel().render();

        transform.identity().translate(position.x + scale.x - 16, position.y, 0).scale(16, scale.y, 1); //  RIGHT

        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState) {
            case STATE_SELECTED :
                tileSheet.bindTile(shader, 5, 1);
                break;
            case STATE_CLICKED :
                tileSheet.bindTile(shader, 2, 4);
                break;
            default :
                tileSheet.bindTile(shader, 2, 1);
                break;
        }
        Assets.getModel().render();
    }

    private void renderCorners(Vector2f position, Vector2f scale, Camera camera, TileSheet tileSheet, Shader shader){
        transform.identity().translate(position.x - scale.x + 16, position.y + scale.y - 16, 0).scale(16, 16, 1); //TOP LEFT

        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState) {
            case STATE_SELECTED :
                tileSheet.bindTile(shader, 3, 0);
                break;
            case STATE_CLICKED :
                tileSheet.bindTile(shader, 0, 3);
                break;
            default :
                tileSheet.bindTile(shader, 0, 0);
                break;
        }
        Assets.getModel().render();

        transform.identity().translate(position.x + scale.x - 16, position.y + scale.y - 16, 0).scale(16, 16, 1); //TOP RIGHT

        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState) {
            case STATE_SELECTED :
                tileSheet.bindTile(shader, 5, 0);
                break;
            case STATE_CLICKED :
                tileSheet.bindTile(shader, 2, 3);
                break;
            default :
                tileSheet.bindTile(shader, 2, 0);
                break;
        }
        Assets.getModel().render();

        transform.identity().translate(position.x - scale.x + 16, position.y - scale.y + 16, 0).scale(16, 16, 1); //BOTTOM LEFT

        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState) {
            case STATE_SELECTED :
                tileSheet.bindTile(shader, 3, 2);
                break;
            case STATE_CLICKED :
                tileSheet.bindTile(shader, 0, 5);
                break;
            default :
                tileSheet.bindTile(shader, 0, 2);
                break;
        }
        Assets.getModel().render();

        transform.identity().translate(position.x + scale.x - 16, position.y - scale.y + 16, 0).scale(16, 16, 1); //BOTTOM RIGHT

        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState) {
            case STATE_SELECTED :
                tileSheet.bindTile(shader, 5, 2);
                break;
            case STATE_CLICKED :
                tileSheet.bindTile(shader, 2, 5);
                break;
            default :
                tileSheet.bindTile(shader, 2, 2);
                break;
        }
        Assets.getModel().render();
    }
}
