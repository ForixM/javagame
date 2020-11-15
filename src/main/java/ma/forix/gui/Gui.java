package ma.forix.gui;

import ma.forix.collision.Collision;
import ma.forix.renderer.Camera;
import ma.forix.renderer.Shader;
import ma.forix.renderer.Window;
import ma.forix.util.Input;

public class Gui {
    private static TextureRenderer textureRenderer;

    private Camera camera;

    private Widget widgets[];

    public Gui(Window window){
        camera = new Camera(window.getWidth(), window.getHeight());

        widgets = new Widget[20];
        textureRenderer = new TextureRenderer(camera, Shader.shader);
    }

    public int insertWidget(Widget widget){
        for (int i = 0; i < widgets.length; i++) {
            if (widgets[i] == null){
                widgets[i] = widget;
                return i;
            }
        }
        return -1;
    }

    public void removeWidget(int widgetId){
        widgets[widgetId] = null;
    }

    public void resizeCamera(Window window){
        camera.setProjection(window.getWidth(), window.getHeight());
    }

    public void update(Input input) {
        for (Widget widget : widgets) {
            if (widget != null) {
                widget.update(input);
                Collision data = widget.getBoundingBox().getCollision(input.getMousePosition());

                if (data.isIntersecting) {
                    if (input.isMouseButtonDown(0)) {
                        widget.onMouseClicked(0);
                    } else if (input.isMouseButtonDown(1)) {
                        widget.onMouseClicked(1);
                    } else if (input.isMouseButtonDown(2)) {
                        widget.onMouseClicked(2);
                    }
                }
            }
        }
    }

    public void render(){
        for (Widget widget : widgets) {
            if (widget != null) {
                Shader.guiTex.bind();
                widget.render(camera, Shader.guiTex);
            }
        }
    }

    public static TextureRenderer getTextureRenderer() {
        return textureRenderer;
    }
}
