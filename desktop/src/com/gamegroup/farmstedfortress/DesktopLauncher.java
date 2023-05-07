package com.gamegroup.farmstedfortress;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.farmstedfortress.MyGdxGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Farmstead Fortress");
		config.setWindowedMode(1280, 720); // set the windowed mode to 1280x720
		new Lwjgl3Application(new MyGdxGame(), config);
	}
}
