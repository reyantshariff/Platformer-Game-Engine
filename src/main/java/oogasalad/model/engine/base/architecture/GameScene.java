package oogasalad.model.engine.base.architecture;

import static oogasalad.model.config.GameConfig.LOGGER;

import java.text.MessageFormat;
import java.util.*;
import oogasalad.model.config.GameConfig;
import oogasalad.model.engine.component.ComponentTag;
import oogasalad.model.engine.component.Camera;

/**
 * The GameScene class is the base class for all game scenes. It manages the game objects and
 * components within the scene. It is responsible for updating the game objects and components every
 * frame.
 *
 * @author Hsuan-Kai Liao, Christian Bepler
 */
public class GameScene {

  private final UUID id;
  private final Map<UUID, GameObject> allObjects;
  private final Map<ComponentTag, List<GameComponent>> allComponents;
  private final Queue<Runnable> subscribedEvents;
  private final Set<GameObject> awakeList;

  private String name;
  private Game game;

  private static final String NO_CAMERA_KEY = "noCamera";
  private static final String CAST_FAILED_KEY = "castFailed";
  private static final String GAME_OBJECT = "GameObject";
  private static final String CAMERA = "Camera";

  /**
   * Constructor for GameScene
   *
   * @param name the name of the scene
   */
  public GameScene(String name) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.allObjects = new HashMap<>();
    this.allComponents = new EnumMap<>(ComponentTag.class);
    for (ComponentTag tag : ComponentTag.values()) {
      allComponents.put(tag, new ArrayList<>());
    }
    this.subscribedEvents = new LinkedList<>();
    this.awakeList = new HashSet<>();
  }

  /**
   * Set the game that this scene belongs to.
   *
   * @param game the game that this scene belongs to
   */
  final void setGame(Game game) {
    this.game = game;
  }

  /**
   * Get the game that this scene belongs to.
   */
  public Game getGame() {
    return game;
  }

  /**
   * Get the name of the scene
   */
  public final String getName() {
    return name;
  }

  /**
   * Set the name of the scene
   */
  public final void setName(String name) {
    this.name = name;
  }

  /**
   * Get the UUID of the scene
   */
  public final UUID getId() {
    return id;
  }

  /**
   * Get the Object based on the UUID
   *
   * @param id the UUID of the object
   */
  public final GameObject getObject(UUID id) {
    return allObjects.get(id);
  }

  /**
   * Get the Object based on the name
   *
   * @param name the name of the object
   */
  public final GameObject getObject(String name) {
    for (GameObject object : allObjects.values()) {
      if (object.getName().equals(name)) {
        return object;
      }
    }
    LOGGER.error(MessageFormat.format(GameConfig.getText("noSuchObject"), name));
    throw new IllegalArgumentException(
        MessageFormat.format(GameConfig.getText("noSuchObject"), name));
  }

  /**
   * Getter to return a Collection of all the GameObjects
   */
  public final Collection<GameObject> getAllObjects() {
    return Collections.unmodifiableCollection(allObjects.values());
  }

  /**
   * Get all the objects in the view of the camera
   *
   * @return a collection of all the objects in the view of the camera
   */
  public final Collection<GameObject> getAllObjectsInView() {
    List<GameComponent> cameraComponents = allComponents.get(ComponentTag.CAMERA);
    if (cameraComponents.isEmpty()) {
      return Collections.emptyList();
    }
    Camera camera = getCamera();
    return camera.getObjectsInView();
  }

  /**
   * Get the camera in the scene
   *
   * @return the camera in the scene
   */
  public final Camera getCamera() {
    try {
      return (Camera) allComponents.get(ComponentTag.CAMERA).get(0);
    } catch (IndexOutOfBoundsException e) {
      LOGGER.error(GameConfig.getText(NO_CAMERA_KEY));
      throw new IllegalArgumentException(GameConfig.getText(NO_CAMERA_KEY), e);
    } catch (ClassCastException e) {
      LOGGER
          .error(MessageFormat.format(GameConfig.getText(CAST_FAILED_KEY), GAME_OBJECT, CAMERA));
      throw new IllegalArgumentException(
          MessageFormat.format(GameConfig.getText(CAST_FAILED_KEY), GAME_OBJECT, CAMERA), e);
    }
  }

  /**
   * @return - Returns whether current scene has a camera or not
   */
  public boolean hasCamera() {
    try {
      getCamera();
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }


  /**
   * Getter to return a Collection of all the GameComponents
   */
  public final Map<ComponentTag, List<GameComponent>> getAllComponents() {
    return Collections.unmodifiableMap(allComponents);
  }

  /**
   * This will be called every frame.
   *
   * @param deltaTime the elapsed time between two frames
   */
  public final void step(double deltaTime) {

    if (hasCamera()) {
      wakeObject(getCamera().getParent());
    }

    runSubscribedEvents();

    // 2. Get all objects in view
    Collection<GameObject> objectsInView = getAllObjectsInView();

    // 3. Update the components of objects in view based on the order
    updateComponents(objectsInView, deltaTime);

    // 4. Update the scene actions
    // TODO: Handle Change Scene Action Here
  }

  private void runSubscribedEvents() {
    while (!subscribedEvents.isEmpty()) {
      subscribedEvents.poll().run();
    }
  }

  private void updateComponents(Collection<GameObject> objectsInView, double deltaTime) {
    for (ComponentTag order : ComponentTag.values()) {
      if (order == ComponentTag.NONE) {
        continue;
      }
      updateObjects(order, objectsInView, deltaTime);
    }
  }

  private void updateObjects(ComponentTag order, Collection<GameObject> objectsInView,
      double deltaTime) {
    for (GameObject object : objectsInView) {
      wakeObject(object);
      for (GameComponent component : object.getComponents(order)) {
        component.update(deltaTime);
      }
    }
  }

  private void wakeObject(GameObject object) {
    if (awakeList.contains(object)) {
      object.wakeUp();
      awakeList.remove(object);
    }
  }

  /**
   * Subscribe the runnable event to the next frame to execute. Events will only be called once and
   * then removed from the subscribed list.
   *
   * @param event the event to be subscribed
   */
  public final void subscribeEvent(Runnable event) {
    subscribedEvents.add(event);
  }

  /**
   * Register the component from the gameObject onto the scene
   *
   * @param gameComponent the component to be registered
   */
  final void registerComponent(GameComponent gameComponent) {
    allComponents.get(gameComponent.componentTag()).add(gameComponent);
  }

  /**
   * Unregister the component from the scene.
   *
   * @param gameComponent the gameComponent to be unregistered
   */
  final void unregisterComponent(GameComponent gameComponent) {
    allComponents.get(gameComponent.componentTag()).remove(gameComponent);
  }

  /**
   * Register the existing gameObject to the scene.
   *
   * @param gameObject the gameObject to be registered.
   */
  public final void registerObject(GameObject gameObject) {
    if (allObjects.containsKey(gameObject.getId())) {
      throw new IllegalArgumentException(
          MessageFormat.format(GameConfig.getText("duplicateGameObject"), gameObject.getName()));
    }

    gameObject.setScene(this);

    // Register components
    for (GameComponent component : gameObject.getAllComponents().values()) {
      registerComponent(component);
    }

    awakeList.add(gameObject);
    allObjects.put(gameObject.getId(), gameObject);
  }

  /**
   * unregister the gameObject specified.
   *
   * @param gameObject the gameObject to be destroyed
   */
  public final void unregisterObject(GameObject gameObject) {
    // Unregister components
    List<GameComponent> componentsToDelete = new ArrayList<>();
    for (ComponentTag order : ComponentTag.values()) {
      for (GameComponent component : allComponents.get(order)) {
        if (component.getParent().equals(gameObject)) {
          componentsToDelete.add(component);
        }
      }
    }
    componentsToDelete.forEach(this::unregisterComponent);

    gameObject.setScene(null);
    allObjects.remove(gameObject.getId());
  }

  /**
   * Change the scene to the specified scene name.
   *
   * @param sceneName the name of the scene to be changed to
   */
  final void changeScene(String sceneName) {
    game.changeScene(sceneName);
  }

  /**
   * Event that will be called when the gameScene is set to active.
   */
  public void onActivated() {
    // NOTE: This method should be overriden if needed.
  }

  /**
   * Event that will be called when the gameScene is set to inactive.
   */
  public void onDeactivated() {
    // NOTE: This method should be overriden if needed.
  }
}
