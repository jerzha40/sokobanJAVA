package com.github.sokobanJAVA;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.bullet.collision.uint_key_func;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.sokobanJAVA.ecs.EntityFactory;
import com.github.sokobanJAVA.ecs.components.MoveComponent;
import com.github.sokobanJAVA.ecs.systems.CollisionSystem;
import com.github.sokobanJAVA.ecs.systems.InputSystem;
import com.github.sokobanJAVA.ecs.systems.MovementSystem;
import com.github.sokobanJAVA.ecs.systems.RenderSystem;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends ApplicationAdapter {
    private static final float VIRTUAL_WIDTH = 500;
    private static final float VIRTUAL_HEIGHT = 500;

    private OrthographicCamera camera;
    private OrthographicCamera UIcamera;
    private Viewport viewport;
    private Viewport UIviewport;
    private SpriteBatch batch;
    private Texture blockTexture;
    private Texture frameboxTexture;
    private Texture wallTexture;
    private Texture crateTexture;
    private Texture playerTexture;

    private PooledEngine engine;
    private EntityFactory factory;
    private Stage stage;

    @Override
    public void create() {
        // 视口与相机
        camera = new OrthographicCamera();
        UIcamera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        UIviewport = new ExtendViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        UIviewport.apply();
        UIcamera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        UIcamera.update();

        viewport.apply();
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();

        //
        stage = new Stage(UIviewport);
        // Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(
                Gdx.files.internal("clean-crispy/skin/clean-crispy-ui.json"));
        TextButton button = new TextButton("+", skin);
        button.setPosition(0, UIviewport.getScreenHeight() - 100);
        button.setSize(100, 100);
        stage.addActor(button);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                camera.zoom /= 1.01f;
                Gdx.app.log("调试", "当前相机缩放：" + camera.zoom);
            }
        });
        TextButton buttonp = new TextButton("-", skin);
        buttonp.setPosition(100, UIviewport.getScreenHeight() - 100);
        buttonp.setSize(100, 100);
        stage.addActor(buttonp);
        buttonp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                camera.zoom *= 1.01f;
                Gdx.app.log("调试", "当前相机缩放：" + camera.zoom);
            }
        });

        float DIR_BT_SIZE = 64;
        TextButton buttonL = new TextButton("L", skin);
        buttonL.setPosition(0, 0);
        buttonL.setSize(DIR_BT_SIZE, DIR_BT_SIZE);
        stage.addActor(buttonL);
        TextButton buttonR = new TextButton("R", skin);
        buttonR.setPosition(DIR_BT_SIZE * 2, 0);
        buttonR.setSize(DIR_BT_SIZE, DIR_BT_SIZE);
        stage.addActor(buttonR);
        TextButton buttonU = new TextButton("U", skin);
        buttonU.setPosition(DIR_BT_SIZE, DIR_BT_SIZE);
        buttonU.setSize(DIR_BT_SIZE, DIR_BT_SIZE);
        stage.addActor(buttonU);
        TextButton buttonD = new TextButton("D", skin);
        buttonD.setPosition(DIR_BT_SIZE, 0);
        buttonD.setSize(DIR_BT_SIZE, DIR_BT_SIZE);
        stage.addActor(buttonD);

        // 渲染器
        batch = new SpriteBatch();

        // 加载资源
        blockTexture = new Texture("libgdx.png");
        frameboxTexture = new Texture("selectbox.png");
        wallTexture = new Texture("Wall.png");
        crateTexture = new Texture("Crate.png");
        playerTexture = new Texture("canopy_icon_white.png");

        // ECS 引擎与工厂
        engine = new PooledEngine();
        factory = new EntityFactory(engine);

        // 添加系统
        engine.addSystem(new RenderSystem(batch));
        engine.addSystem(new InputSystem());
        engine.addSystem(new MovementSystem(engine, 64));
        engine.addSystem(new MovementSystem(engine, 64));
        engine.addSystem(new CollisionSystem(engine));

        // 创建一个方块实体，位置 (100,100)
        factory.createRectangle(200, VIRTUAL_HEIGHT, frameboxTexture, 200, 200);
        factory.createRectangle(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, frameboxTexture, 200, 200);
        factory.createRectangle(VIRTUAL_WIDTH, 0, frameboxTexture, 200, 200);
        // factory.createPlayer(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, blockTexture);

        factory.createWall(64, 64 * 3, wallTexture, 64, 64);
        factory.createBox(64 * 2, 64, crateTexture, 64, 64);
        factory.createPlayer(64, 64, playerTexture, 64, 64);
        InputSystem inputSys = engine.getSystem(InputSystem.class);
        buttonL.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("调试", "L");
                inputSys.injectDirection(-1, 0);
            }
        });
        buttonR.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("调试", "R");
                inputSys.injectDirection(1, 0);
            }
        });
        buttonU.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("调试", "U");
                inputSys.injectDirection(0, 1);
            }
        });
        buttonD.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("调试", "D");
                inputSys.injectDirection(0, -1);
            }
        });
        Gdx.input.setInputProcessor(new MyInputProcessor());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.5f, 0.01f, 0.2f, 1f);
        viewport.apply();
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_ADD)) {
            camera.zoom /= 1.01f;
            Gdx.app.log("调试", "当前相机缩放：" + camera.zoom);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            camera.zoom *= 1.01f;
            Gdx.app.log("调试", "当前相机缩放：" + camera.zoom);
        }
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        // 更新 ECS：会调用所有系统的 processEntity()
        engine.update(Gdx.graphics.getDeltaTime());

        UIviewport.apply();
        UIcamera.update();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        UIviewport.update(width, height, true);
        UIcamera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
    }

    @Override
    public void dispose() {
        batch.dispose();
        blockTexture.dispose();
        engine.removeAllEntities(); // 如有必要清理
    }

}
