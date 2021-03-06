package system;

import achievements.*;
import personalisation.Person;
import personalisation.PersonDatabase;
import rewards.UseValcrowShoes;
import utility.Permission;
import utility.RequirementsNotMetException;

public class ShowCase {

	public static void main(String[] args) throws RequirementsNotMetException {
		testPersonCreation();
		testPersonAchievementsRemoveInactiveAchievement();
		testPersonAchievementsAddMultipleSuccessfulAchievements();
	}

	private static void testPersonCreation() {

		System.out.println(">>> testPersonCreation");
		new Person("Erni", "J�rgens", 79, true, Permission.ADMIN);
		printPersonDataBase();

		new Person("Martha", "Hauhammer", 46, false, Permission.USER);
		printPersonDataBase();

		new Person("John", "Noodle", 5, true, Permission.GUEST);
		printPersonDataBase();
	}

	private static void testPersonAchievementsRemoveInactiveAchievement() throws RequirementsNotMetException {

		System.out.println(">>> testPersonAchievementsRemoveInactiveAchievement");
		// TODO allow creation of non-Admin users
		Person person = new Person("Brutus", "Judas", 100, false, Permission.GUEST);
		person.addAchievement(new Rob(person));
		person.addAchievement(new Crawl(person));
		person.addAchievement(new Run(person));
		person.addAchievement(new BindShoeLaces(person));
		person.addAchievement(new UseValcrowShoes(person));

		printPersonDataBase();
	}

	private static void testPersonAchievementsAddMultipleSuccessfulAchievements() throws RequirementsNotMetException {

		System.out.println(">>> testPersonAchievementsAddMultipleSuccessfulAchievements");
		Person person = new Person("Brutus", "Judas", 100, true, Permission.ADMIN);
		person.addAchievement(new Rob(person));
		person.addAchievement(new Crawl(person));
		person.addAchievement(new Stand(person));
		person.addAchievement(new Walk(person));
		person.addAchievement(new Run(person));
		person.addAchievement(new Sprint(person));
		person.addAchievement(new Run(person));
		person.addAchievement(new Balance(person));

		printPersonDataBase();
	}

	private static void printPersonDataBase() {
		PersonDatabase.printDatabase();
		System.out.println();
	}
}
