/**
 * A base obstacle, all other obstacles such as boxes or planks are descendants of this class
 */
package com.haha.bitrunner.Actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Obstacle {
	public static final float SIZE = 1f;
	public Rectangle   bounds = new Rectangle();
    public Vector2 position = new Vector2();
    public boolean filled = true;
}