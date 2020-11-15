package ma.forix.world;

import ma.forix.collision.AABB;
import ma.forix.game.Factory;
import ma.forix.item.Item;
import ma.forix.renderer.*;
import ma.forix.tile.Tile;
import ma.forix.tile.TileContainer;
import ma.forix.tile.tilentities.TileEntity;
import ma.forix.util.TilePos;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

public class World {

    private byte[] tiles;
    private byte[] items;
    private int width;
    private int height;
    private int scale;
    private int renderX;
    private int renderY;
    private int generationCompletion = 0;

    private AABB[] tile_bounding_boxes;
    private AABB[] item_bounding_boxes;

    private TileEntity[] tileEntities;
    private TileContainer[] tileContainers;

    private SimplexNoise noise;

    private Matrix4f world;

    public World(String world){
        try {
            BufferedImage tile_sheet = ImageIO.read(new File("./res/levels/"+world+"_tiles.png"));
            //BufferedImage entity_sheet = ImageIO.read(new File("./res/levels/"+world+"_entity.png"));

            width = tile_sheet.getWidth();
            height = tile_sheet.getHeight();
            scale = 32;

            this.world = new Matrix4f().setTranslation(new Vector3f(0));
            this.world.scale(scale);

            int[] colorTileSheet = tile_sheet.getRGB(0, 0, width, height, null, 0, width);

            tiles = new byte[width * height];
            Arrays.fill(tiles, (byte) 2);
            items = new byte[width * height];
            Arrays.fill(items, (byte)-1);

            tile_bounding_boxes = new AABB[width*height];
            item_bounding_boxes = new AABB[width*height];

            for (int y = 0; y < height; y++){
                for (int x = 0; x < width; x++){
                    int blue = (colorTileSheet[x + y * width]) & 0xFF;
                    int green = (colorTileSheet[x + y * width] >> 8) & 0xFF;
                    int red = (colorTileSheet[x + y * width] >> 16) & 0xFF;

                    Tile tile = null;
                    if (red == 255){
                        tile = Tile.grass;
                    } else
                    if (green == 255){
                        tile = Tile.stone;
                    } else
                    if (blue == 255){
                        tile = Tile.sand;
                    }
                    if (tile != null) {
                        setTile(tile, x, y);
                    }
                }
            }

            generateCollisions();
            tileContainers = new TileContainer[width*height];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage map;

    public World() {
        width = 128;
        height = 128;
        scale = 32;

        map = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        tiles = new byte[width * height];
        Arrays.fill(tiles, (byte) 2);

        items = new byte[width * height];
        Arrays.fill(items, (byte)-1);

        tile_bounding_boxes = new AABB[width * height];
        item_bounding_boxes = new AABB[width * height];

        double seed = Math.random()*1000;

        noise = new SimplexNoise(width, 0.5, (int) seed);

        world = new Matrix4f().setTranslation(new Vector3f(0));
        world.scale(scale);
        tileContainers = new TileContainer[width*height];
        tileEntities = new TileEntity[width*height];
    }

    public void generateTerrain(){
        Thread t = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < tiles.length; i++) {
                    int x = i/width;
                    int y = i%width;
                    if (noise.getNoise(x, y) < -0.2){
                        tiles[i] = Tile.water.getId();
                        map.setRGB(y, x, 0x0000FF);
                    } else if (noise.getNoise(x, y) < 0.05) {
                        tiles[i] = Tile.sand.getId();
                        map.setRGB(y, x, 0xFFFF00);
                    }else {
                        if (noise.getNoise(x, y) < 0.5){
                            tiles[i] = Tile.grass.getId();
                            map.setRGB(y, x, 0x00FF00);
                        } else {
                            tiles[i] = Tile.stone.getId();
                            map.setRGB(y, x, 0x000000);
                        }
                    }
                    generationCompletion = (int) Math.ceil((200*i)/tiles.length);
                }
                try {
                    ImageIO.write(map, "png", new File("C:\\Users\\forix\\Desktop\\map.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                generateCollisions();
                System.out.println("Terrain generated");
                setTile(Tile.chest, 5, 5);
            }
        };
        t.start();
    }

    public int getGenerationCompletion() {
        return generationCompletion;
    }

    private void generateCollisions(){
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                Tile tile = getTile(j, i);
                if (tile.isSolid()){
                    tile_bounding_boxes[i*width+j] = new AABB(new Vector2f(j*2, -i*2), new Vector2f(1, 1));
                } else {
                    tile_bounding_boxes[i*width+j] = null;
                }
            }
        }
    }

    public void setTile(Tile tile, int x, int y){
        tiles[x + y*width] = tile.getId();
        if (tile.isSolid())
            tile_bounding_boxes[x + y * width] = new AABB(new Vector2f(x*2, -y*2), new Vector2f(1, 1));
        else
            tile_bounding_boxes[x + y * width] = null;
        if (tile.haveContainer()) {
            addTileContainer((TileContainer) new TileContainer(tile.getStorageSize()).addObject(Item.stick), new TilePos(x, y));
            System.out.println("have container at pos: x:"+x+" y:"+y);
            System.out.println("tile.getTileContainerPos() = " + tile.getTileContainerPos());
        } else {
            System.out.println("havn't container");
        }
        addTileEntity(tile.createTileEntity(), new TilePos(x, y));
    }

    public void setItem(Item item, int x, int y){
        items[x + y*width] = item.getId();
            item_bounding_boxes[x + y * width] = new AABB(new Vector2f(x*2, -y*2), new Vector2f(1, 1));
    }

    public void removeItem(int x, int y){
        items[x+y*width] = -1;
        item_bounding_boxes[x+y*width] = null;
    }

    public Matrix4f getWorldMatrix(){
        return world;
    }

    int posX, posY;
    Tile t;
    public void renderTiles(TileRenderer render, Shader shader, Camera camera, Window window){
        posX = ((int) camera.getPosition().x + (window.getWidth() / 2)) / (scale * 2);
        posY = ((int) camera.getPosition().y - (window.getHeight() / 2)) / (scale * 2);

        renderX = window.getWidth()/scale/2+2;
        renderY = window.getHeight()/scale/2+2;
        for (int i = 0; i < renderX; i++){
            for (int j = 0; j < renderY; j++){
                t = getTile(i-posX, j+posY);
                if (t != null){
                    render.renderTile(t, i-posX, -j-posY, shader, world, camera);
                }
            }
        }
    }

    Item item;
    public void renderItems(ItemRenderer render, Shader shader, Camera camera, Window window){
        posX = ((int) camera.getPosition().x + (window.getWidth() / 2)) / (scale * 2);
        posY = ((int) camera.getPosition().y - (window.getHeight() / 2)) / (scale * 2);

        for (int i = 0; i < renderX; i++) {
            for (int j = 0; j < renderY; j++) {
                item = getItem(i-posX, j+posY);
                if (item != null){
                    render.renderItem(item, i-posX, -j-posY, shader, world, camera);
                }
            }
        }
    }

    Vector3f pos;
    public Vector3f correctCamera(Camera camera, Window window){
        pos = camera.getPosition();

        int w = -width * scale * 2;
        int h = height * scale * 2;

        if (pos.x > -(window.getWidth()/2)+scale)
            pos.x = -(window.getWidth()/2)+scale;
        if (pos.x < w + (window.getWidth() / 2) + scale)
            pos.x = w + (window.getWidth() / 2) + scale;

        if (pos.y < (window.getHeight()/2)-scale)
            pos.y = (window.getHeight()/2)-scale;
        if (pos.y > h - (window.getHeight() / 2) - scale)
            pos.y = h - (window.getHeight() / 2) - scale;

        return pos;
    }

    public Vector2f correctViewPort(Vector2f viewPos, Window window){
        int w = -width * scale * 2;
        int h = height * scale * 2;

        if (viewPos.x > -(window.getWidth()/2)+scale)
            viewPos.x = -(window.getWidth()/2)+scale;
        if (viewPos.x < w + (window.getWidth() / 2) + scale)
            viewPos.x = w + (window.getWidth() / 2) + scale;

        if (viewPos.y < (window.getHeight()/2)-scale)
            viewPos.y = (window.getHeight()/2)-scale;
        if (viewPos.y > h - (window.getHeight() / 2) - scale)
            viewPos.y = h - (window.getHeight() / 2) - scale;

        return viewPos;
    }

    public Tile getTile(int x, int y){
        try {
            return Tile.tiles[tiles[x+y*width]];
        } catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    public Item getItem(int x, int y){
        try {
            return Item.items[items[x + y * width]];
        } catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    public AABB getTileBoundingBox(int x, int y){
        try {
            return tile_bounding_boxes[x+y*width];
        } catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    public AABB getItemBoundingBox(int x, int y){
        try {
            return item_bounding_boxes[x+y*width];
        } catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    public int getScale() {
        return scale;
    }

    public int getRenderX() {
        return renderX;
    }

    public int getRenderY() {
        return renderY;
    }

    public Matrix4f getWorld() {
        return world;
    }

    public Vector2i getWorldPosition(Window win, Camera camera){
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
        int scale = getScale()*2;
        glfwGetCursorPos(win.getWindow(), posX, posY);
        Vector2f viewPort = correctViewPort(new Vector2f(0, 0), win);
        Vector2f posCam = new Vector2f(Math.abs(camera.getPosition().x+Math.abs(viewPort.x))/scale, Math.abs(camera.getPosition().y-Math.abs(viewPort.y))/scale);
        int excessX = (int) (Math.abs(camera.getPosition().x+608)%scale);
        int excessY = (int) (Math.abs(camera.getPosition().y-328)%scale);
        int x = (int) ((posX.get()+excessX)/scale);
        int y = (int) ((posY.get()+excessY)/scale);
        return new Vector2i((int)posCam.x+x, (int)posCam.y+y);
    }

    public void removeTile(int x, int y){
        setTile(Tile.bedrock, x, y);
        removeTileEntity(new TilePos(x, y));
    }

    public void scaleUp(float increment){
        scale *= increment;
        world.scale(increment);
    }

    public TileContainer getTileContainer(TilePos tilePos){
        if (tilePos.x < width && tilePos.y < height){
            int key = tilePos.x*tilePos.y;
            return tileContainers[key];
        }
        return null;
    }

    public void addTileContainer(TileContainer tileContainer, TilePos tilePos){
        int key = tilePos.x*tilePos.y;
        if (tileContainers[key] == null) {
            //Factory.gui.insertWidget(tileContainer.getInventory());
            tileContainers[key] = tileContainer;
        }
    }

    public void removeTileContainer(TilePos tilePos){
        int key = tilePos.x*tilePos.y;
        if (tileContainers[key] != null)
            tileContainers[key] = null;
    }

    public TileEntity getTileEntity(TilePos tilePos){
        if (tilePos.x < width && tilePos.y < height){
            int key = tilePos.x+tilePos.y*width;
            return tileEntities[key];
        }
        return null;
    }

    public void addTileEntity(TileEntity tileEntity, TilePos tilePos){
        int key = tilePos.x+tilePos.y*width;
        System.out.println("key = " + key);
        System.out.println("tilePos.x = " + tilePos.x);
        System.out.println("tilePos.y = " + tilePos.y);
        if (tileEntities[key] == null) {
            tileEntities[key] = tileEntity;
        }
    }

    public void removeTileEntity(TilePos tilePos){
        int key = tilePos.x+tilePos.y*width;
        if (tileEntities[key] != null)
            tileEntities[key] = null;
    }

    public TileEntity[] getTileEntities() {
        return tileEntities;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
