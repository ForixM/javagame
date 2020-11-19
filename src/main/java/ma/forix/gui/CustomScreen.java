package ma.forix.gui;

import ma.forix.collision.Collision;
import ma.forix.game.Factory;
import ma.forix.gui.widgets.Slot;
import ma.forix.renderer.Camera;
import ma.forix.renderer.Shader;
import ma.forix.renderer.Window;
import ma.forix.util.Input;
import org.joml.Vector2f;

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
                if (widget instanceof Slot){
                    Shader.gui.bind();
                    widget.render(camera, Shader.gui);
                } else {
                    Shader.guiTex.bind();
                    widget.render(camera, Shader.guiTex);
                }
            }
        }
    }

    public void update(Input input){
        for (Widget widget : widgets){
            if (widget != null){
                widget.update(input);
                Collision data = widget.getBoundingBox().getCollision(input.getMousePosition());
                if (data.isIntersecting) {
                    if (input.isAnyMouseButtonPressed()){
                        widget.onMouseClicked();
                        onMouseClicked(widget);
                    }
                    if (input.isAnyKeyPressed()){
                        widget.onKeyPressed();
                        onKeyPressed(widget);
                    }
                }
            }
        }
    }

    public void addWidget(Widget widget){
        widgets.add(widget);
    }

    public void onMouseClicked(Widget widget){

    }

    public void onKeyPressed(Widget widget){

    }

    public boolean isIntersecting(Vector2f mousePosition){
        for (Widget widget : widgets) {
            Collision data = widget.getBoundingBox().getCollision(mousePosition);
            if (data.isIntersecting){
                return true;
            }
        }
        return false;
    }
}
