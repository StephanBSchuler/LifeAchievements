package Achievements;

import Personalisation.Person;
import Utility.RequirementsNotMetException;

public class BeButtler extends Achievement {

	public BeButtler(Person person) throws RequirementsNotMetException {
		super("BeButtler", 0, person);

		this.addRequirement("BeWaiter");
		this.addRequirement("PolishCuttlery");
		this.addRequirement("PolishShoes");
		this.addRequirement("BindTie");
	}

}
