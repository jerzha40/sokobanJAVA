package com.github.sokobanJAVA;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.sokobanJAVA.ecs.EntityFactory;
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
    private Viewport viewport;
    private SpriteBatch batch;
    private Texture blockTexture;
    private Texture frameboxTexture;

    private PooledEngine engine;
    private EntityFactory factory;
    private Stage stage;

    @Override
    public void create() {
        // 视口与相机
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        viewport.apply();
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();

        //
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(
                Gdx.files.internal("clean-crispy/skin/clean-crispy-ui.json"));
        TextButton button = new TextButton("+", skin);
        button.setPosition(100, 100);
        button.setSize(200, 200);
        stage.addActor(button);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                camera.zoom /= 1.01f;
                Gdx.app.log("调试", "当前相机缩放：" + camera.zoom);
            }
        });
        TextButton buttonp = new TextButton("-", skin);
        buttonp.setPosition(300, 300);
        buttonp.setSize(200, 200);
        stage.addActor(buttonp);
        buttonp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                camera.zoom *= 1.01f;
                Gdx.app.log("调试", "当前相机缩放：" + camera.zoom);
            }
        });

        Gdx.input.setInputProcessor(new MyInputProcessor());
        Gdx.input.setInputProcessor(stage);

        // 渲染器
        batch = new SpriteBatch();

        // 加载资源
        blockTexture = new Texture("libgdx.png");
        frameboxTexture = new Texture("selectbox.png");

        // ECS 引擎与工厂
        engine = new PooledEngine();
        factory = new EntityFactory(engine);

        // 添加系统
        engine.addSystem(new RenderSystem(batch));

        // 创建一个方块实体，位置 (100,100)
        factory.createRectangle(200, VIRTUAL_HEIGHT, frameboxTexture, 200, 200);
        factory.createRectangle(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, frameboxTexture, 200, 200);
        factory.createRectangle(VIRTUAL_WIDTH, 0, frameboxTexture, 200, 200);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.5f, 0.01f, 0.2f, 1f);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
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
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
    }

    @Override
    public void dispose() {
        batch.dispose();
        blockTexture.dispose();
        engine.removeAllEntities(); // 如有必要清理
    }

}
