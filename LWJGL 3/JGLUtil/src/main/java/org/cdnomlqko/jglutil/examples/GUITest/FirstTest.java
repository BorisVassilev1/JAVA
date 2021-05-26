package org.cdnomlqko.jglutil.examples.GUITest;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;

public class FirstTest extends Application {

	@Override
	protected void configure(Configuration config) {
		config.setTitle("Dear ImGui is Awesome!");
	}

	@Override
	public void process() {
		ImGui.text("Hello, World!");
	}

	public static void main(String[] args) {
		launch(new FirstTest());
	}

}
