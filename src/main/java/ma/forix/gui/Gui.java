package ma.forix.gui;

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

    public void insertWidget(Widget widget){
        for (int i = 0; i < widgets.length; i++) {
            if (widgets[i] == null){
                widgets[i] = widget;
                return;
            }
        }
    }

    public void resizeCamera(Window window){
        camera.setProjection(window.getWidth(), window.getHeight());
    }

    public void update(Input input) {
        for (int i = 0; i < widgets.length; i++) {
            if (widgets[i] != null){
                widgets[i].update(input);
            }
        }
    }

    public void render(){
        for (int i = 0; i < widgets.length; i++) {
            if (widgets[i] != null){
                Shader.guiTex.bind();
                widgets[i].render(camera, Shader.guiTex);
            }
        }
    }

    public static TextureRenderer getTextureRenderer() {
        return textureRenderer;
    }
}
