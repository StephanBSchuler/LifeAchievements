package achievements;

import personalisation.Person;
import utility.RequirementsNotMetException;

public class Babble extends Achievement {

	private static int level = 1;

	public Babble(Person person) throws RequirementsNotMetException {
		super("Babble", level, person);
		
		this.addRequirement("Drink");
	}

}
