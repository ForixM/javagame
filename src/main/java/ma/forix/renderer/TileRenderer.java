package ma.forix.renderer;

import ma.forix.tile.Tile;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashMap;

public class TileRenderer {

    private HashMap<String, Texture> tile_textures;
    private Model model;

    final int TOP_LEFT = 0;
    final int TOP_RIGHT = 1;
    final int BOTTOM_RIGHT = 2;
    final int BOTTOM_LEFT = 3;

    public TileRenderer(){
        tile_textures = new HashMap<String, Texture>();

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

        for (int i = 0; i < Tile.tiles.length; i++){
            if (Tile.tiles[i] != null) {
                String texture = Tile.tiles[i].getTexture();
                if (! tile_textures.containsKey(texture)) {
                    tile_textures.put(texture, new Texture(texture + ".png"));
                }
            }
        }
    }

    public void renderTile(Tile tile, int x, int y, Shader shader, Matrix4f world, Camera camera){
        shader.bind();
        if (tile_textures.containsKey(tile.getTexture()))
            tile_textures.get(tile.getTexture()).bind(0);

        Matrix4f tile_pos = new Matrix4f().translate(new Vector3f(x*2, y*2, 0));
        Matrix4f target = new Matrix4f();

        camera.getProjection().mul(world, target);
        target.mul(tile_pos);

        shader.setUniform("sampler", 0);
        shader.setUniform("projection", target);

        model.render();
    }

}
