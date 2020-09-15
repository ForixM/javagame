package ma.forix.renderer;

import ma.forix.item.Item;
import ma.forix.tile.Tile;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashMap;

public class ItemRenderer {

    private HashMap<String, Texture> item_textures;
    private Model model;

    final int TOP_LEFT = 0;
    final int TOP_RIGHT = 1;
    final int BOTTOM_RIGHT = 2;
    final int BOTTOM_LEFT = 3;

    public ItemRenderer(){
        item_textures = new HashMap<>();

        float[] vertices = new float[] {
                -1f, 1f,
                1f, 1f,
                1f, -1f,
                -1f, -1f
        };

        float[] texture_coords = new float[]{
                0, 0,
                1, 0,
                1, 1,
                0, 1
        };

        int[] indices = new int[]{
                TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT,
                TOP_LEFT, BOTTOM_RIGHT, BOTTOM_LEFT
        };

        model = new Model(vertices, texture_coords, indices);

        for (int i = 0; i < Item.items.length; i++){
            if (Item.items[i] != null) {
                String texture = Item.items[i].getTexture();
                if (!item_textures.containsKey(texture)) {
                    System.out.println(texture+": "+Item.items[i].isTile());
                    item_textures.put(texture, new Texture(texture + ".png"));
                }
            }
        }
    }

    public void renderItem(Item item, float x, float y, Shader shader, Matrix4f world, Camera camera){
        shader.bind();
        if (item_textures.containsKey(item.getTexture()))
            item_textures.get(item.getTexture()).bind(0);

        Matrix4f tile_pos = new Matrix4f().translate(new Vector3f(x*2, y*2, 0)).scale(0.5f);
        Matrix4f target = new Matrix4f();

        camera.getProjection().mul(world, target);
        target.mul(tile_pos);

        shader.setUniform("sampler", 0);
        shader.setUniform("projection", target);

        model.render();
    }
}
