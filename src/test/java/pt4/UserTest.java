package pt4;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import pt4.flotsblancs.database.model.User;

public class UserTest extends DatabaseTestWrapper {

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
		assertEquals(User.getConnected(), user);
		// Le equals est "deep" (Donc pas par référence mais par certains attributs)
		// grâce au :
		// @EqualsAndHashCode
		// dans User.java
	}

	@Test
	public void testLogout() {
		// Force login
		User.logIn("test_user_123456789", "test_password");
		var testUser = User.getConnected();
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
}
