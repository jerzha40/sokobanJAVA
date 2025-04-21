package com.github.sokobanJAVA;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter {
    @Override
    public boolean keyDown(int keycode) {
        if (keycode >= 0 && keycode <= Input.Keys.MAX_KEYCODE) {
            System.out.println("按下的键是: " + keycode);
        } else {
            System.out.println("无效的键码: " + keycode);
        }
        return false;
    }

    // 实现其他必要的方法
}