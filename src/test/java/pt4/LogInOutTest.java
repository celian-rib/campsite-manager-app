package pt4;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import pt4.flotsblancs.database.Database;
import pt4.flotsblancs.database.model.User;

@TestInstance(Lifecycle.PER_CLASS)
public class LogInOutTest {

	private static User testUser;

	@BeforeAll
	public void setUp() throws SQLException {
		System.out.println("Connexion à la base de données");
		assertTrue(Database.getInstance().isConnected());

		System.out.println("Création utilisateur de test");

		testUser = new User();
		testUser.setName("TestName");
		testUser.setFirstName("TestFirstName");
		testUser.setAdmin(true);
		testUser.setLogin("test_user_123456789");
		testUser.setPassword(User.sha256("test_password"));

		// On regarde si l'utilisteur n'est pas déjà dans la base
		// (Ca peut être le cas si un test précédent na pas fonctionné)
		List<User> matching = Database.getInstance().getUsersDao().queryForMatching(testUser);
		if (matching.size() > 0) {
			System.out.println("Utilisateur de test déjà dans la BD (Recyclage)");
			testUser = matching.get(0);
		} else {
			// Si l'utilisateur de test n'est pas dans la base on l'ajoute
			Database.getInstance().getUsersDao().create(testUser);
		}
	}

	@Test
	public void testLogin() {
		// Force logout
		User.logOut();

		boolean logInResult;
		// Mauvais mdp
		logInResult = User.logIn("test_user_123456789", "test_password_wrong");
		assertFalse(logInResult, "Login avec un mauvais identifiant");
		assertFalse(User.isConnected());
		assertNull(User.getConnected());

		// Mauvais login
		logInResult = User.logIn("test_user_123456789_wrong", "test_password");
		assertFalse(logInResult, "Login avec mauvais mot de passe");
		assertFalse(User.isConnected());
		assertNull(User.getConnected());

		// Login correct
		logInResult = User.logIn("test_user_123456789", "test_password");
		assertTrue(logInResult);
		assertTrue(User.isConnected());
		assertNotNull(User.getConnected());
		assertEquals(User.getConnected(), testUser);
		// Le equals est "deep" (Donc pas par référence mais par certains attributs)
		// grâce au :
		// @EqualsAndHashCode(callSuper=false)
		// dans User.java
	}

	@Test
	public void testLogout() {
		// Force login
		User.logIn("test_user_123456789", "test_password");
		testUser = User.getConnected();
		assertNotNull(testUser);
		assertTrue(User.isConnected());

		// Logout et vérification
		User.logOut();
		assertFalse(User.isConnected());
		assertNull(User.getConnected());
	}

	@Test
	public void testPasswordHashed() {
		// Force login
		User.logIn("test_user_123456789", "test_password");
		assertEquals(User.sha256("test_password"), User.getConnected().getPassword());
		assertNotEquals("test_password", User.getConnected().getPassword());
	}

	@AfterAll
	public void tearDown() throws SQLException {
		System.out.println("Supression utilisateur de test");
		Database.getInstance().getUsersDao().delete(testUser);
	}

}
