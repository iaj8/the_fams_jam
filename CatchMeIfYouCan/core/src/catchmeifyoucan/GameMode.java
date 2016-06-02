/*
 * GameMode.java
 *
 * This is the primary class file for running the game.  You should study this file for
 * ideas on how to structure your own root class. This class follows a
 * model-view-controller pattern fairly strictly.
 *
 * Author: Walker M. White
 * Based on original GameX Ship Demo by Rama C. Hoetzlein, 2002
 * LibGDX version, 1/16/2015
 */
package catchmeifyoucan;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

/**
 * The primary controller class for the game.
 *
 * While GDXRoot is the root class, it delegates all of the work to the player mode
 * classes. This is the player mode class for running the game. In initializes all
 * of the other classes in the game and hooks them together.  It also provides the
 * basic game loop (update-draw).
 */
public class GameMode implements ModeController {
  // GRAPHICS AND SOUND RESOURCES
  // Pathnames to texture  and sound assets
  /** The background image for the battle */
  private static final String BACKGROUND_FILE = "images/white.png";
  /** Texture for the me*/
  private static final String ME_TEXTURE = "images/circle_white.png";
  /** Number of rows in the me image filmstrip */
  private static final int ME_ROWS = 1;
  /** Number of columns in this me image filmstrip */
  private static final int ME_COLS = 1;
  /** Number of elements in this me image filmstrip */
  private static final int ME_SIZE = 1;
  /** How sensitive to mouse proximity the jump should be */
  private static final float SENSITIVITY = 1.1f;
  boolean DEBUG_PRINT = false;

  // Asset loading is handled statically, so these are static variables
  /** The background image for the battle */
  private static Texture background;
  /** Texture for the me*/
  private static Texture meTexture;
  float width;
  float height;

  /**
   * Preloads the texture and sound information for the game.
   *
   * All instance of the game use the same assets, so this is a static method.
   * This keeps us from loading the assets multiple times.
   *
   * The asset manager for LibGDX is asynchronous.  That means that you
   * tell it what to load and then wait while it loads them.  This is
   * the first step: telling it what to load.
   *
   * @param manager Reference to global asset manager.
   */
  public static void PreLoadContent(AssetManager manager) {
    manager.load(BACKGROUND_FILE,Texture.class);
    manager.load(ME_TEXTURE,Texture.class);
  }

  /**
   * Loads the texture information for the me.
   *
   * All instance of the game use the same assets, so this is a static method.
   * This keeps us from loading the assets multiple times.
   *
   * The asset manager for LibGDX is asynchronous.  That means that you
   * tell it what to load and then wait while it loads them.  This is
   * the second step: extracting assets from the manager after it has
   * finished loading them.
   *
   * @param manager Reference to global asset manager.
   */
  public static void LoadContent(AssetManager manager) {
    background    = manager.get(BACKGROUND_FILE, Texture.class);
    meTexture   = manager.get(ME_TEXTURE, Texture.class);
    // Make the me content prettier
    meTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
  }

  /**
   * Unloads the texture information for the me.
   *
   * This method erases the static variables.  It also deletes the
   * associated textures from the assert manager.
   *
   * @param manager Reference to global asset manager.
   */
  public static void UnloadContent(AssetManager manager) {
    manager.unload(BACKGROUND_FILE);
    manager.unload(ME_TEXTURE);
  }

    /** The Me to catch */
  protected Me me;

  /** Store the bounds to enforce the playing region */
  private Rectangle bounds;

  /**
   * Creates a new game with a playing field of the given size.
   *
   * This constructor initializes the models and controllers for the game.  The
   * view has already been initialized by the root class.
   *
   * @param width The width of the game window
   * @param height The height of the game window
   */
  public GameMode(float width, float height) {
    // Initialize the photons.
    bounds = new Rectangle(0,0,width,height);

    // Initalize the Me
    me = new Me(width*(1.0f / 3.0f), height*(1.0f / 2.0f));
    me.setFilmStrip(new FilmStrip(meTexture,ME_ROWS,ME_COLS,ME_SIZE));
    me.setColor(new Color(1.0f, 0.25f, 0.25f, 1.0f));  // Red, but makes texture easier to see

    this.width = width;
    this.height = height;

  }

  /**
   * Read user input, calculate physics, and update the models.
   *
   * This method is HALF of the basic game loop.  Every graphics frame
   * calls the method update() and the method draw().  The method update()
   * contains all of the calculations for updating the world, such as
   * checking for collisions, gathering input, and playing audio.  It
   * should not contain any calls for drawing to the screen.
   */
  @Override
  public void update() {
	  if (Gdx.input.isTouched()){
		  if (DEBUG_PRINT){
			  DEBUG_PRINT = false;
			  System.out.println("CLICK");
			  System.out.println("Mouse: " + Gdx.input.getX() + " , " + Gdx.input.getY());
			  System.out.println("Me: " + me.getPosition().x + " , " + me.getPosition().y);
//			  System.out.println("Me size is " + me.getXSize() + " , " + me.getYSize());
			  System.out.println("width: " + width + " , " + "height: " + height);
			  System.out.println("*************************************************************");  
		  }
		  
	  } else {
		  DEBUG_PRINT = true;
	  }
	  float x = Gdx.input.getX();
	  float y = height - Gdx.input.getY();
	  float me_x_min = me.getPosition().x-(me.getXSize()/2)*SENSITIVITY;
	  float me_x_max = me.getPosition().x+(me.getXSize()/2)*SENSITIVITY;
	  float me_y_min = me.getPosition().y-(me.getYSize()/2)*SENSITIVITY;
	  float me_y_max = me.getPosition().y+(me.getYSize()/2)*SENSITIVITY;
	  boolean x_cond = x <= me_x_max && x >= me_x_min;
	  boolean y_cond = y <= me_y_max && y >= me_y_min;
	  if(x_cond && y_cond){
		  float x1 = (float)Math.random()*width;
		  float y1 = (float)Math.random()*height;
		  me.setPosition(new Vector2(x1,y1));
	  }
  }

  /**
   * Draw the game on the provided GameCanvas
   *
   * There should be no code in this method that alters the game state.  All
   * assignments should be to local variables or cache fields only.
   *
   * @param canvas The drawing context
   */
  @Override
  public void draw(GameCanvas canvas) {
    canvas.draw(background, Color.WHITE,canvas.getWidth()/2,
    		canvas.getHeight()/2,background.getWidth()/2,background.getHeight()/2,2.7f,2.7f);
    me.drawMe(canvas);
  }

  /**
   * Dispose of all (non-static) resources allocated to this mode.
   */
  public void dispose() {
    // Garbage collection here is sufficient.  Nothing to do
  }

  /**
   * Resize the window for this player mode to the given dimensions.
   *
   * This method is not guaranteed to be called when the player mode
   * starts.  If the window size is important to the player mode, then
   * these values should be passed to the constructor at start.
   *
   * @param width The width of the game window
   * @param height The height of the game window
   */
  public void resize(int width, int height) {
    bounds.set(0,0,width,height);
  }

}


//Mouse coordinates top left is 0,0 Me coordinates bottom left is 0,0
