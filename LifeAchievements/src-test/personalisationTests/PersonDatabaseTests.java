package personalisationTests;

import org.junit.Before;
import org.junit.Test;

public class PersonDatabaseTests {

	@Before
	public void setUp() throws Exception {
		// fill database with test persons
		PersonDatabase.addPerson(new Person("admin","no1",1,false,Permission.ADMIN));
		PersonDatabase.addPerson(new Person("user","no1",2,false,Permission.USER));
		PersonDatabase.addPerson(new Person("guest","no1",3,false,Permission.GUEST));
		PersonDatabase.addPerson(new Person("editable","no1",4,true,Permission.GUEST));
	}

	// test all methods
	@Test
	public void testCleanGuests() {
		// TODO make sure proper rights granted for this test
		List<Person> persons = PersonDatabase.getPersons();
		int databaseSize = persons.size();
		// TODO adding two guests in a row might remove the previous depending on user rights
		// TODO therefore should be tested with different users with different rights
		PersonDatabase.cleanGuests();
		// TODO assert that no guests are present in database anymore
		persons = PersonDatabase.getPersons();
		boolean noGuest = true;
		for(Person p: persons) {
			if(p.getPermission()==Permission.GUEST) {
				noGuest = false;
			}
		}
		Assert.assertTrue(noGuest);
	}
	
	// test
	@Test
	public void removePerson() {
		
	}

}