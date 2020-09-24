package ma.forix.game;

import imgui.*;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import ma.forix.assets.Assets;
import ma.forix.entity.player.Player;
import ma.forix.gui.Gui;
import ma.forix.gui.Text;
import ma.forix.renderer.*;
import ma.forix.tile.Tile;
import ma.forix.util.Timer;
import ma.forix.util.Transform;
import ma.forix.world.World;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class Factory {

    private Thread mainGameThread;

    private Window window;
    private World world;
    private Camera camera;
    private TileRenderer tileRenderer;
    private ItemRenderer itemRenderer;
    public static Gui gui;
    public static Player player;

    private ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private Text text;

    public static void main(String[] args) {
        new Factory();
    }

    private byte[] loadFromResources(final String fileName) {
        try (InputStream is = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(fileName));
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            final byte[] data = new byte[16384];

            int nRead;
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            return buffer.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void initImGui(){
        ImGui.createContext();
        final ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null);
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);  // Enable Keyboard Controls
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);      // Enable Docking
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);    // Enable Multi-Viewport / Platform Windows
        io.setConfigViewportsNoTaskBarIcon(true);
        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        // Glyphs could be added per-font as well as per config used globally like here
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());

        // Add a default font, which is 'ProggyClean.ttf, 13px'
        fontAtlas.addFontDefault();

        // ------------------------------------------------------------
        // Use freetype instead of stb_truetype to build a fonts texture
        ImGuiFreeType.buildFontAtlas(fontAtlas, ImGuiFreeType.RasterizerFlags.LightHinting);

        // When viewports are enabled we tweak WindowRounding/WindowBg so platform windows can look identical to regular ones.
        if (io.hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final ImGuiStyle style = ImGui.getStyle();
            style.setWindowRounding(0.0f);
            style.setColor(ImGuiCol.WindowBg, ImGui.getColorU32(ImGuiCol.WindowBg, 1));
        }

        imGuiGlfw.init(window.getWindow(), true);
        imGuiGl3.init("#version 130");
    }

    public Factory(){
        mainGameThread = new Thread(){
            @Override
            public void run() {
                super.run();
                Window.setCallbacks();
                window = new Window(1280, 720, false);
                window.createWindow("You've been tiled cheh");

                initImGui();

                world = new World();
                world.generateTerrain();
                camera = new Camera(window.getWidth(), window.getHeight());
                tileRenderer = new TileRenderer();
                itemRenderer = new ItemRenderer();
                Assets.initAsset();
                gui = new Gui(window);
                player = new Player(world, new Transform());
                gui.insertWidget(player.getInventory());
                gui.insertWidget(player.getHotbar());

                world.setItem(Tile.plank.getItem(), 3, 3);
                world.setItem(Tile.plank.getItem(), 5, 3);
                world.setItem(Tile.plank.getItem(), 15, 3);
                world.setItem(Tile.bedrock.getItem(), 10, 5);


                int fps = 144;
                int frames = 0;
                double frameTime = 1.0/(double)fps;
                double time = Timer.getTime();
                double passed = 0;

                while (!window.shouldClose()) {
                    while (Timer.getTime()-time < frameTime){
                        if (passed>=1.0){
                            passed = 0;
                            System.out.println("frames = " + frames);
                            frames = 0;
                        }
                    }
                    time = Timer.getTime();
                    passed+=frameTime;
                    update(frameTime);
                    render();
                    frames++;
                }
                Assets.deleteAsset();
                glfwTerminate();
            }
        };
        mainGameThread.start();
    }

    private void update(double frameTime){
        window.update();
        player.update((float) frameTime, window, camera, world);
        world.correctCamera(camera, window);
        gui.update(window.getInput());
    }

    private void render(){
        glClearColor(0, 0, 0, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        world.renderTiles(tileRenderer, Shader.shader, camera, window);
        world.renderItems(itemRenderer, Shader.shader, camera, window);

        player.render(Shader.shader, camera, world);

        gui.render();
        if (player.getOnMouse() != null)
            Gui.getTextureRenderer().renderItem(player.getOnMouse(), player.getMouseX(), player.getMouseY());

        /*imGuiGlfw.newFrame();
        ImGui.newFrame();
        ImGui.setNextWindowSize(400, 400);
        ImGui.begin("title");
        final ImGuiViewport mainViewport = ImGui.getMainViewport();
        ImGui.setNextWindowSize(600, 300, ImGuiCond.Once);
        ImGui.setNextWindowPos(mainViewport.getWorkPosX() + 10, mainViewport.getWorkPosY() + 10, ImGuiCond.Once);



        int dukeTexture = 0;
        try {
            if (dukeTexture == 0)
                dukeTexture = loadTexture(ImageIO.read(new File("res/player.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Draw an image in the bottom-right corner of the window
        final float xPoint = ImGui.getWindowPosX() + ImGui.getWindowSizeX() - 100;
        final float yPoint = ImGui.getWindowPosY() + ImGui.getWindowSizeY();
        //ImGui.getWindowDrawList().addImage(dukeTexture, xPoint, yPoint - 180, xPoint + 100, yPoint);
        ImGui.text("oui");
        //ImGui.getWindowDrawList().addText("Test Bro");

        ImGui.end();

        ImGui.render();
        //ImGui.text("test");

        imGuiGl3.renderDrawData(ImGui.getDrawData());
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            glfwMakeContextCurrent(backupWindowPtr);
        }*/
        window.swapBuffers();
    }

    private int loadTexture(final BufferedImage image) {
        final int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        final ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); // 4 for RGBA, 3 for RGB
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                final int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();

        final int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        return textureID;
    }
}
