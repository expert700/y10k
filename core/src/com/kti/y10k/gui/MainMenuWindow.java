package com.kti.y10k.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kti.y10k.MainLoop;
import com.kti.y10k.gui.helpers.TextButtonHelper;
import com.kti.y10k.io.SaveLoader;
import com.kti.y10k.io.SaveWriter;
import com.kti.y10k.utilities.Logger;
import com.kti.y10k.utilities.managers.WindowManager;

import java.nio.file.Files;
import java.nio.file.Paths;

public class MainMenuWindow extends WindowWrapper {
    private TextButton newGame;
    private TextButton resumeButton;
    private TextButton creditButton;
    private TextButton closeButton;
    private TextButton saveGame;
    private TextButton loadGame;

    public MainMenuWindow() {
        super("Menu",0.8f,0.4f,0.1f,0.2f);
        newGame = new TextButton("New Galaxy", TextButtonHelper.textButtonStyle);
        newGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainLoop.instance.inMenu = false;
                MainLoop.instance.started = true;
            }
        });
        WindowManager.components.addActor(newGame);

        resumeButton = new TextButton("Resume", TextButtonHelper.textButtonStyle);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (MainLoop.instance.started) {
                    MainLoop.instance.inMenu = false;
                } else if (!Files.notExists(Paths.get("save"))) {
                    Thread t = new Thread() {
                        @Override
                        public void run() {
                            try {
                                if(SaveLoader.load("recent") == 1) {
                                    Logger.log(Logger.LogLevel.INFO, "Saved Successfully!");
                                    WindowManager.newPopup(
                                            "Galaxy Loaded \nSuccessfully", 0.45f, 0.45f);
                                } else {
                                    WindowManager.newPopup(
                                            "Error: Load Galaxy! \n Check Logs!", 0.45f, 0.45f);
                                }
                            } catch (Exception e) {
                                Logger.log(e.getStackTrace());
                            }
                        }
                    };

                    t.start();
                    MainLoop.instance.started = true;
                    MainLoop.instance.inMenu = false;
                }
            }
        });
        WindowManager.components.addActor(resumeButton);

        creditButton = new TextButton("Credits", TextButtonHelper.textButtonStyle);
        creditButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                WindowManager.creditWindow.setVisible(true);
            }
        });
        WindowManager.components.addActor(creditButton);

        closeButton = new TextButton("Close", TextButtonHelper.textButtonStyle);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainLoop.instance.dispose();
                System.exit(0);
            }
        });
        WindowManager.components.addActor(closeButton);

        saveGame = new TextButton("Save Galaxy", TextButtonHelper.textButtonStyle);
        saveGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (MainLoop.instance.started) {
                    Thread t = new Thread() {
                        @Override
                        public void run() {
                            try {
                                if(SaveWriter.save() == 1) {
                                    Logger.log(Logger.LogLevel.INFO, "Saved Successfully!");
                                    WindowManager.newPopup(
                                            "Galaxy Saved \nSuccessfully", 0.45f, 0.45f);
                                } else {
                                    WindowManager.newPopup(
                                            "Error: Save Galaxy! \n Check Logs!", 0.45f, 0.45f);
                                }
                            } catch (Exception e) {
                                Logger.log(e.getStackTrace());
                            }
                        }
                    };

                    t.start();
                }
            }
        });
        WindowManager.components.addActor(saveGame);

        loadGame = new TextButton("Load Galaxy", TextButtonHelper.textButtonStyle);
        loadGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            if(SaveLoader.load() == 1) {
                                Logger.log(Logger.LogLevel.INFO, "Saved Successfully!");
                                WindowManager.newPopup(
                                        "Galaxy Loaded \nSuccessfully", 0.45f, 0.45f);
                            } else {
                                WindowManager.newPopup(
                                        "Error: Load Galaxy! \n Check Logs!", 0.45f, 0.45f);
                            }
                        } catch (Exception e) {
                            Logger.log(e.getStackTrace());
                        }
                    }
                };

                t.start();
            }
        });
        WindowManager.components.addActor(loadGame);
    }

    @Override
    public void draw(SpriteBatch uiBatch, BitmapFont font, float x, float y, float width, float height) {
        resumeButton.setPosition(x + 5, y + height / 2 + 4*font.getLineHeight() - 5);
        if (MainLoop.instance.started || !Files.notExists(Paths.get("save")))
            resumeButton.draw(uiBatch, 1);
        else
            resumeButton.draw(uiBatch, 0.4f);

        newGame.setPosition(x + 5, y + height / 2 + 3*font.getLineHeight() - 5);
        newGame.draw(uiBatch, 1);

        saveGame.setPosition(x + 6, y + height / 2 + 2 * font.getLineHeight() - 5);
        if (MainLoop.instance.started) {
            saveGame.draw(uiBatch, 1);
        } else {
            saveGame.draw(uiBatch, 0.4f);
        }

        loadGame.setPosition(x + 5, y + height / 2 + font.getLineHeight() - 5);
        loadGame.draw(uiBatch, 1);

        creditButton.setPosition(x + 5, y + font.getLineHeight() + 5);
        creditButton.draw(uiBatch, 1);

        closeButton.setPosition(x + 5, y + 5);
        closeButton.draw(uiBatch, 1);
    }
}
