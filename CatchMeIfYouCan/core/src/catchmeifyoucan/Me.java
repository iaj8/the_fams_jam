/*
 * Me.java
 *
 * This is what you try and catch - can be a circle or any shape with any writing in it.
 */
package catchmeifyoucan;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;

/**
 * Model class representing thing to catch
 */
public class Me {

    // Private constants to avoid use of "magic numbers"
    /** Amount to decay forward thrust over time */
    private static final float FORWARD_DAMPING = 0.9f;
  
    /** Default amount to scale the me size */
    private static final float DEFAULT_SCALE = 0.2f;
    /** Amount to scale the me size */
    private static final float scale = DEFAULT_SCALE;
    
  /** Position of the me */
  private Vector2 pos;
  /** Velocity of the me */
  private Vector2 vel;
  /** Color to tint this me */
  private Color  tint;
 
  
  /** Current angle of the me */
//    protected float ang;
  
  // Asset references.  These should be set by GameMode
  /** Reference to me's sprite for drawing */
    private FilmStrip meSprite;

    // ACCESSORS
    /**
     * Returns the image filmstrip for this me
     * 
     * This value should be loaded by the GameMode and set there. However, we
     * have to be prepared for this to be null at all times
     *
     * @return the image texture for this me
     */
    public FilmStrip getFilmStrip() {
      return meSprite;
    }
    
    /**
     * Sets the image texture for this me
     * 
     * This value should be loaded by the GameMode and set there. However, we
     * have to be prepared for this to be null at all times
     *
     * param value the image texture for this me
     */
    public void setFilmStrip(FilmStrip value) {
      meSprite = value;
    }
    
  /**
   * Returns the position of this me.
   *
   * This is location of the center pixel of the me on the screen.
   *
   * @return the position of this me
   */
  public Vector2 getPosition() { 
    return pos;  
  }
  
  /**
   * Sets the position of this me.
   *
   * This is location of the center pixel of the me on the screen.
   *
   * @param value the position of this me
   */
  public void setPosition(Vector2 value) { 
    pos.set(value);
  }

  /**
   * Returns the velocity of this me.
   *
   * This value is necessary to control momementum in me movement.
   *
   * @return the velocity of this me
   */
  public Vector2 getVelocity() {
    return vel;  
  }

  /**
   * Sets the velocity of this me.
   *
   * This value is necessary to control momementum in me movement.
   *
   * @param value the velocity of this me
   */
  public void setVelocity(Vector2 value) { 
    vel.set(value);
  }
  
  /**
   * Returns the tint color for this me.
   *
   * We can change how an image looks without loading a new image by 
   * tinting it differently.
   *
   * @returns the tint color
   */
  public Color getColor() {
    return tint;  
  }
  
  /**
   * Sets the tint color for this me.
   *
   * We can change how an image looks without loading a new image by 
   * tinting it differently.
   *
   * @param value the tint color
   */
  public void setColor(Color value) { 
    tint.set(value);
  }
  

  /**
   * Returns the length of the me from the center to the x boundary.
   *
   * This value is necessary to resolve collisions.
   *
   * @returns the me x length
   */
  public float getXSize() {
    return meSprite.getRegionWidth()*scale;
  }
  
  /**
   * Returns the length of the me from the center to the y boundary.
   *
   * This value is necessary to resolve collisions.
   *
   * @returns the me y length
   */
  public float getYSize() {
	  return meSprite.getRegionHeight()*scale;
  }
  
  /**
   * Creates a new me at the given location.
   *
   * @param x The initial x-coordinate of the center
   * @param y The initial y-coordinate of the center
   */
    public Me(float x, float y) {
        // Set the position of this me.
        this.pos = new Vector2(x,y);

        // We start at rest.
        vel = new Vector2();

        //Set current me image
        tint  = new Color(Color.WHITE);
    }

  /**
   * Moves the me by the specified amount.  
   * 
   * Forward is the amount to move forward. This method performs no collision detection.  
   * Collisions are resolved afterwards.
   *
   * @param forward Amount to move forward
   * @param bounds  The rectangular bounds of the playing field
   */
  public void move(float forward, Rectangle bounds){

    // Process the me thrust.
    if (forward != 0.0f) {
      // Thrust key pressed; increase the me velocity.
      vel.add(forward, forward);
      if (vel.len() > 65){
    	  vel.setLength(65);
      }
    } else {  
      // Gradually slow the me down
      vel.scl(FORWARD_DAMPING);
    }

    // Move the me, updating it.

    // Move the me position by the me velocity
    pos.add(vel);
    adjustToBounds(bounds);

    //Increment the refire readiness counter
  }
    
    
    /**
     * Nudge the me to ensure it does not do out of view.
     *
     * This code bounces the me off walls.  You will replace it as part of
     * the lab.
     *
   * @param bounds  The rectangular bounds of the playing field
   */
    private void adjustToBounds(Rectangle bounds) {
    	//Ensure the me doesn't go out of view. Bounce off walls.
    	if (pos.x <= bounds.x) {
    		//vel.x = -vel.x;
    		pos.x = bounds.width;
    	} else if (pos.x >= bounds.width) {
    		//vel.x = -vel.x;
    		pos.x = bounds.x;
    	}

    	if (pos.y <= bounds.y) {
    		//vel.y = -vel.y;
    		pos.y = bounds.height;
    	} else if (pos.y >= bounds.height) {
    		//vel.y = -vel.y;
    		pos.y = bounds.y;
    	}    
    }

  
  /**
   * Draws the me to the given GameCanvas. 
   *
   * @param canvas The drawing canvas.
   */
    public void drawMe(GameCanvas canvas) {
    	if (meSprite == null) {
    		return;
    	}
    	// For placement purposes, put origin in center.
        float ox = 0.5f * meSprite.getRegionWidth();
        float oy = 0.5f * meSprite.getRegionHeight();
    	canvas.draw(meSprite, tint, ox, oy, pos.x, pos.y, scale, scale);
    	canvas.drawMeText("Catch Me If You Can", pos.x-getXSize()/2+10, pos.y+getYSize()/2-20, 65);
    }
}
