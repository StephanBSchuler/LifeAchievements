package achievements;

import personalisation.Person;
import utility.RequirementsNotMetException;

public class Swallow extends Achievement {

	private static int level = 4;

	public Swallow(Person person) throws RequirementsNotMetException {
		super("Swallow", level, person);

		this.addAchievement(null);
	}

}
