package com.gamegroup.farmstedfortress;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.farmsteadfortress.FarmsteadFortress;

public class DesktopLauncher {
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Farmstead Fortress");
		config.setWindowedMode(1600, 900);
		new Lwjgl3Application(new FarmsteadFortress(), config);
	}
}