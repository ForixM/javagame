package ma.forix.renderer;

import ma.forix.util.Input;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;

public class Window {

    private long window;

    private int width, height;
    private boolean fullscreen;

    private Input input;

    public static void setCallbacks(){
        glfwSetErrorCallback(new GLFWErrorCallback() {
            @Override
            public void invoke(int error, long description) {
                throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
            }
        });
    }

    public Window(){
        this(640, 480, false);
    }

    public Window(int width, int height, boolean fullscreen){
        if (!glfwInit()){
            System.err.println("GLFW Failed to initialize !");
            System.exit(1);
        }
        setSize(width, height);
        setFullscreen(fullscreen);
    }

    public void createWindow(String title){
        GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (fullscreen)
            setSize(vid.width(), vid.height());

        window = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : 0,  0);
        System.out.println("Window id: "+window);

        if (window == 0) {
            throw new IllegalStateException("Failed to create window !");
        }

        if (!fullscreen) {
            glfwSetWindowPos(window, (vid.width() - width) / 2, (vid.height() - height) / 2);
            glfwShowWindow(window);
        }
        glfwMakeContextCurrent(window);

        input = new Input(window);
        GL.createCapabilities();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public boolean shouldClose(){
        return glfwWindowShouldClose(window);
    }

    public void swapBuffers(){
        glfwSwapBuffers(window);
    }

    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public void update(){
        input.update();
        glfwPollEvents();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public long getWindow() {
        return window;
    }

    public Input getInput() {
        return input;
    }
}
