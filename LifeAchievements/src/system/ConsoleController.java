package system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import achievements.*;
import personalisation.Person;
import personalisation.PersonDatabase;
import personalisation.UserDatabase;
import rewards.UseValcrowShoes;
import rewards.WatchTV;
import utility.Permission;
import utility.RequirementsNotMetException;

public class ConsoleController {

	private static String confirmPhrase = "yes";
	private static boolean end = false;
	private static boolean printRequests = false;
	private static int counter = 0;
	private static Hashtable<String, Integer> commands = new Hashtable<String, Integer>();
	private static Hashtable<String, Integer> secrets = new Hashtable<String, Integer>();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static int i = 0, timeoutCounter = 30;

	protected static void run() throws RequirementsNotMetException, IOException {

		while (!end) {

			if (i >= timeoutCounter) {
				if (!refreshSession()) {
					end = true;
				}
			} else {

				++i;
				System.out.print(i + "|Enter a command ('help' for commands):");
				String in = br.readLine();
				switch (getCommand(in)) {
				case -1:
					break;
				case 1:
					createPerson();
					break;
				case 2:
					end = true;
					System.out.println("Ending");
					break;
				case 3:
					printDatabase();
					break;
				case 4:
					AccessController.login();
					break;
				case 5:
					AccessController.logout();
					break;
				case 6:
					printCommands();
					break;
				case 7:
					addAchievement();
					break;
				case 8:
					cleanGuests();
					break;
				case 9:
					AccessController.getStatus();
					break;
				case 10:
					selectPerson();
					break;
				case 11:
					togglePrintRequests();
					break;
				case 12:
					registerUser();
					break;
				}
			}
		}
	}

	private static boolean refreshSession() throws IOException {
		boolean refresh = false;
		System.out.print("Continue?" + getConfirmPhrase());
		String confirm = br.readLine();
		if (confirm.equals(confirmPhrase)) {
			refresh = true;
			i = 0;
		} else {
			System.out.println("******************************");
			System.out.println("* Ending Life Achievements.  *");
			System.out.println("*      Til senere! ;-)       *");
			System.out.println("******************************");
		}
		return refresh;
	}

	private static void registerUser() throws IOException {
		// TODO Auto-generated method stub
		UserDatabase.registerUser(AccessController.getCurrentUser());
	}

	private static void togglePrintRequests() {
		if (printRequests) {
			printRequests = false;
		} else {
			printRequests = true;
		}
	}

	public static boolean printRequests() {
		return printRequests;
	}

	private static void selectPerson() {
		if (Permission.getPermission("ConsoleController.selectPerson")) {
			boolean valid = false;
			int id = -1;
			while (!valid) {
				try {
					System.out.print("Enter ID:");
					id = Integer.parseInt(br.readLine());
					if (id != -1) {
						valid = true;
					}
				} catch (NumberFormatException e) {
				} catch (IOException e) {
				} finally {
					if (!valid) {
						System.out.println("Invalid Format!");
					}
				}
			}
			AccessController.selectPerson(id);
		}
	}

	private static void cleanGuests() {
		if (Permission.getPermission("ConsoleController.cleanGuests")) {
			PersonDatabase.cleanGuests();
		}
	}

	private static void createPerson() throws IOException {
		boolean continueCreation = true;
		if (!AccessController.isLoggedInAsAdmin() && AccessController.getCurrentPerson() != null) {
			System.out.println("Can have only 1 person at a time.\nDelete current person?" + getConfirmPhrase());
			String delete = br.readLine();
			if (delete.equals(confirmPhrase)) {
				// TODO only remove if admin requests this specific removal
				// PersonDatabase.removePerson(AccessController.getCurrentPerson()
				// .getId(), AccessController.getAdmin().getPerson());
				AccessController.setCurrentPerson(null);
			} else {
				System.out.println("Creation cancelled.");
				continueCreation = false;
			}
		}
		if (continueCreation) {
			String firstName = "user", lastName = "" + (++counter);
			int age = 0;
			boolean valid = false;
			while (!valid) {
				System.out.print("Enter first name:");
				firstName = br.readLine();
				if (!firstName.equals("")) {
					valid = true;
				}
			}
			valid = false;
			while (!valid) {
				System.out.print("Enter last name:");
				lastName = br.readLine();
				if (!lastName.equals("")) {
					valid = true;
				}
			}
			valid = false;
			while (!valid) {
				try {
					System.out.print("Enter age:");
					age = Integer.parseInt(br.readLine());
					valid = true;
				} catch (NumberFormatException nfe) {
					System.out.println("Invalid Format!");
				}
			}

			boolean editable = false;
			Permission permission = Permission.GUEST;
			if (AccessController.isLoggedInAsUser() || AccessController.isLoggedInAsAdmin()) {
				permission = Permission.USER;
				if (AccessController.isLoggedInAsAdmin()) {
					System.out.print("Editable person?" + getConfirmPhrase());
					String editString = br.readLine();
					if (editString.equals(confirmPhrase)) {
						editable = true;
					}
					System.out.print("Admin rights?" + getConfirmPhrase());
					String permString = br.readLine();
					if (permString.equals(confirmPhrase)) {
						permission = Permission.ADMIN;
					}
				}
			}
			if (AccessController.getCurrentUser() == null) {
				createGuestPerson(firstName, lastName, age);
			} else {
				Person person = new Person(firstName, lastName, age, editable, permission);
				AccessController.setCurrentPerson(person);
			}
			System.out.println("Person created: " + AccessController.getCurrentPerson().getFirstName() + " "
					+ AccessController.getCurrentPerson().getLastName() + " ["
					+ AccessController.getCurrentPerson().getPermission() + "]");
		}
	}

	private static void createGuestPerson(String firstName, String lastName, int age) {
		AccessController.loginTempUser();
		Person person = new Person(firstName, lastName, age, false, Permission.GUEST);
		AccessController.setCurrentPerson(person);
		AccessController.logoutTempUser();
	}

	public static String getConfirmPhrase() {
		return "(confirm with '" + confirmPhrase + "')";
	}

	private static int getCommand(String s) {
		if (commands.isEmpty() || secrets.isEmpty()) {
			fillCommandList();
		}

		try {
			return commands.get(s);
		} catch (Exception e) {
			System.out.print("Command not found.");
			try {
				if (secrets.get(s) != null) {
					System.out.println(" ;-)");
				}
				return secrets.get(s);
			} catch (Exception e2) {
				System.out.println(" ");
				return -1;
			}
		}
	}

	private static void fillCommandList() {

		commands.put("help", 6);
		commands.put("login", 4);
		commands.put("logout", 5);
		commands.put("new", 1);
		commands.put("add", 7);
		commands.put("end", 2);
		commands.put("exit", 2);
		commands.put("status", 9);
		commands.put("register", 12);
		secrets.put("toggle", 11);
		secrets.put("clean", 8);
		secrets.put("show", 3);
		secrets.put("?", 6);
		secrets.put("h", 6);
		secrets.put("select", 10);

	}

	private static void printCommands() {
		fillCommandList();
		Iterator<Entry<String, Integer>> it;
		Map.Entry<String, Integer> entry;
		it = commands.entrySet().iterator();
		while (it.hasNext()) {
			entry = it.next();
			System.out.println(entry.getKey().toString());
		}
	}

	private static void printDatabase() {
		// TODO print current person stats incl. achievements if not admin
		PersonDatabase.printDatabase();
	}

	private static void addAchievement() throws IOException, RequirementsNotMetException {
		String achievement;
		if (AccessController.getCurrentUser() == null) {
			System.out.println("Create person first!");
			createPerson();
		}
		Person current = AccessController.getCurrentPerson();
		if (current != null) {
			System.out.print("Enter achievement:");
			achievement = br.readLine();
			current.addAchievement(getAchievement(achievement));
		} else {
			System.out.println("No person in use.");
		}
	}

	private static Achievement getAchievement(String achievement) throws RequirementsNotMetException {
		switch (achievement) {
		case "Rob":
			return new Rob(AccessController.getCurrentPerson());
		case "Crawl":
			return new Crawl(AccessController.getCurrentPerson());
		case "Stand":
			return new Stand(AccessController.getCurrentPerson());
		case "Walk":
			return new Walk(AccessController.getCurrentPerson());
		case "Run":
			return new Run(AccessController.getCurrentPerson());
		case "Sprint":
			return new Sprint(AccessController.getCurrentPerson());
		case "Balance":
			return new Balance(AccessController.getCurrentPerson());
		case "BindShoeLaces":
			return new BindShoeLaces(AccessController.getCurrentPerson());
		case "Giggle":
			return new Giggle(AccessController.getCurrentPerson());
		case "Grow":
			return new Grow(AccessController.getCurrentPerson());
		case "Laugh":
			return new Laugh(AccessController.getCurrentPerson());
		case "UseValcrowShoes":
			return new UseValcrowShoes(AccessController.getCurrentPerson());
		case "Speak":
			return new Speak(AccessController.getCurrentPerson());
		case "Read":
			return new Read(AccessController.getCurrentPerson());
		case "WatchTV":
			return new WatchTV(AccessController.getCurrentPerson());
		case "DryDishes":
			return new DryDishes(AccessController.getCurrentPerson());
		case "BeButtler":
			return new BeButtler(AccessController.getCurrentPerson());
		case "BeWaiter":
			return new BeWaiter(AccessController.getCurrentPerson());
		case "BindTie":
			return new BindTie(AccessController.getCurrentPerson());
		case "PolishCuttlery":
			return new PolishCuttlery(AccessController.getCurrentPerson());
		case "PolishShoes":
			return new PolishShoes(AccessController.getCurrentPerson());
		case "Babble":
			return new Babble(AccessController.getCurrentPerson());
		case "Drink":
			return new Drink(AccessController.getCurrentPerson());
		case "Swallow":
			return new Swallow(AccessController.getCurrentPerson());
		default:
			System.out.println("Achievement '" + achievement + "' not available.");
			return null;
		}
	}
}
