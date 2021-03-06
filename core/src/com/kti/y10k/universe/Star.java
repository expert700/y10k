package com.kti.y10k.universe;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.kti.y10k.utilities.managers.AssetManager;
import com.kti.y10k.utilities.managers.GalaxyConstManager;

public class Star {
    private static final TextureRegion STAR_TEXTURE = new TextureRegion(AssetManager.requestTexture("star"));
    private static final float STAR_SIZE = 60.0f;

    private static Vector3 use0 = new Vector3(0,0,0);
    private static Vector3 use1 = new Vector3(0,0,0);

    private float sizeMod;
    private Vector3 position;

    public Star(float x, float y, float z, float sizeMod) {
        position = new Vector3(
                x * GalaxyConstManager.requestConstant("x_squish"),
                y * GalaxyConstManager.requestConstant("y_squish"),
                z * GalaxyConstManager.requestConstant("z_squish"));
        this.sizeMod = sizeMod;
    }

    public void render(SpriteBatch batch, Camera camera) {
        use0.x = camera.position.x + camera.direction.x;
        use0.y = camera.position.y + camera.direction.y;
        use0.z = camera.position.z + camera.direction.z;

        if (use0.dst2(position) < camera.position.dst2(position)) {
            use1.x = position.x;
            use1.y = position.y;
            use1.z = position.z;
            camera.project(use1);

            float dst = position.dst(camera.position);

            Color c = batch.getColor();

            if (STAR_SIZE / dst > 2.0f) {
                //TODO neat color things?

                batch.setColor(c.r, c.g, c.b, c.a);

                batch.draw(STAR_TEXTURE, use1.x - STAR_SIZE / 2 / dst * sizeMod, use1.y - STAR_SIZE / 2 / dst * sizeMod,
                        STAR_SIZE / dst * sizeMod, STAR_SIZE / dst * sizeMod);
            } else {
                batch.setColor(c.r, c.g, c.b, 1.0f < 600 / dst ? 1.0f : 600 / dst);
                batch.draw(STAR_TEXTURE, use1.x - 1 * sizeMod, use1.y - 1 * sizeMod,
                        2 * sizeMod, 2 * sizeMod);
            }

            batch.setColor(c);
        }
    }

    @Override
    public String toString() {
        return "Star " + position.x + " " + position.y + " " + position.z + " " + sizeMod;
    }

    public boolean verify() {
        return  !(Float.isNaN(position.x) || Float.isNaN(position.y) || Float.isNaN(position.z));
    }
}