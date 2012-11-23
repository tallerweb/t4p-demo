package tallerweb.t4p.demo;

import gk.jfilter.JFilter;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Assert;
import org.junit.Test;

import t4p.model.User;
import t4p.persistence.DaoFactory;
import t4p.persistence.TweetDao;
import t4p.persistence.UserDao;

public class DaoTests {

	@Test
	public void queSePuedeLoguearSinFrameworks() {

		Assert.assertTrue(validarUsuario("CapitanKidd", "clave"));

		Assert.assertFalse(validarUsuario("pepe", "clave"));
		Assert.assertFalse(validarUsuario("CapitanKidd", "pepe"));

	}

	private boolean validarUsuario(String username, String password) {
		UserDao userDao = DaoFactory.getInstance().getUserDao();
		Collection<User> a = userDao.findAll();

		for (User theUser : a) {
			if (username.equals(theUser.getUsername())
					&& password.equals(theUser.getPassword())) {
				return true;
			}
		}

		return false;
	}

	@Test
	public void queSePuedeLoguearConJFilter() {

		Assert.assertTrue(validarUsuarioConJFilter("CapitanKidd", "clave"));

		Assert.assertFalse(validarUsuarioConJFilter("pepe", "clave"));
		Assert.assertFalse(validarUsuarioConJFilter("CapitanKidd", "pepe"));

	}

	private boolean validarUsuarioConJFilter(String username, String password) {

		UserDao userDao = DaoFactory.getInstance().getUserDao();
		Collection<User> a = userDao.findAll();

		JFilter<User> filter = new JFilter<User>(a, User.class);

		ArrayList<User> matches = (filter.filter(
				"{'$and':[{'username':'?1', 'password':'?2'}]}", username,
				password).out(new ArrayList<User>()));

		return matches.size() > 0;
	}

	@Test
	public void queSePuedeLoguearConCommonsCollections() {

		Assert.assertTrue(validarUsuarioConCommonsCollections("CapitanKidd",
				"clave"));

		Assert.assertFalse(validarUsuarioConCommonsCollections("pepe", "clave"));
		Assert.assertFalse(validarUsuarioConCommonsCollections("CapitanKidd",
				"pepe"));

	}

	private boolean validarUsuarioConCommonsCollections(final String username,
			final String password) {

		UserDao userDao = DaoFactory.getInstance().getUserDao();
		Collection<User> a = userDao.findAll();

		User theUser = (User)CollectionUtils.find(a, new Predicate() {
			public boolean evaluate(Object o) {
				User localUser = (User) o;
				if (username.equals(localUser.getUsername())
						&& password.equals(localUser.getPassword())) {
					return true;
				}
				return false;
			}
		});

		return theUser != null;
	}

	@Test
	public void queSePuedePedirTodosLosTweets() {

		TweetDao tweetDao = DaoFactory.getInstance().getTweetDao();
		Assert.assertEquals(7, tweetDao.findAll().size());

	}

}
