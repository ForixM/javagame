package ma.forix.gui;

import ma.forix.collision.Collision;
import ma.forix.game.Factory;
import ma.forix.renderer.Camera;
import ma.forix.renderer.Shader;
import ma.forix.renderer.Window;
import ma.forix.util.Input;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomScreen {

    private List<Widget> widgets;
    public Camera camera;
    private String name;

    public CustomScreen(Window window, String name){
        widgets = new ArrayList<>(20);
        camera = new Camera(window.getWidth(), window.getHeight());
        this.name = name;
        Factory.displayCustomScreen(this);
    }

    public void draw(){
        for (Widget widget : widgets){
            if (widget != null) {
                Shader.guiTex.bind();
                widget.render(camera, Shader.guiTex);
            }
        }
    }

    public void update(Input input){
        for (Widget widget : widgets){
            if (widget != null){
                widget.update(input);
                Collision data = widget.getBoundingBox().getCollision(input.getMousePosition());
                if (data.isIntersecting) {
                    if (input.isMouseButtonPressed(0)) {
                        widget.onMouseClicked(0);
                    } else if (input.isMouseButtonPressed(1)) {
                        widget.onMouseClicked(1);
                    } else if (input.isMouseButtonPressed(2)) {
                        widget.onMouseClicked(2);
                    }
                }
            }
        }
    }

    public void addWidget(Widget widget){
        widgets.add(widget);
    }

    public boolean onMouseClicked(Input input){
        return input.isMouseButtonDown(0) || input.isMouseButtonDown(1) || input.isMouseButtonDown(2);
    }
}
