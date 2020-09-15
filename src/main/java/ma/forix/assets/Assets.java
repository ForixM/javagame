package ma.forix.assets;

import ma.forix.renderer.Model;

public class Assets {
    private static Model model;

    public static Model getModel(){
        return model;
    }

    static final int TOP_LEFT = 0;
    static final int TOP_RIGHT = 1;
    static final int BOTTOM_RIGHT = 2;
    static final int BOTTOM_LEFT = 3;

    public static void initAsset(){
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
    }

    public static void deleteAsset(){
        model = null;
    }
}
