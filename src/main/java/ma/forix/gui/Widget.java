package ma.forix.gui;

import ma.forix.renderer.Camera;
import ma.forix.renderer.Shader;
import ma.forix.util.Input;

public interface Widget {

    public void render(Camera camera, Shader shader);

    public void update(Input input);
}
