package system;

import java.io.IOException;

import utility.RequirementsNotMetException;

public class ExecuteLifeAchievement {

	public static void main(String[] args) throws RequirementsNotMetException, IOException {
		System.out.println("*******************************");
		System.out.println("*Welcome to Life Achievements.*");
		System.out.println("* <Type 'help' for commands>  *");
		System.out.println("*******************************");
		ConsoleController.run();
	}
}
