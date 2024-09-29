package org.m4nu;

import org.jasypt.util.password.StrongPasswordEncryptor;

import io.quarkus.security.UnauthorizedException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

@Path("/user")
public class UserResource {
	@POST
	@Path("/register")
	@Transactional
	public User createUser(User user) {
		user.persist();
		return user;
	}

	@POST
	@Path("/login")
	@Transactional
	public User loginUser(User user) {
		User loggedUser = User.find("username = ?1", user.username).firstResult();

		if (loggedUser == null) {
			throw new NotFoundException();
		}

		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

		if (!passwordEncryptor.checkPassword(user.password, loggedUser.password)) {
			throw new UnauthorizedException();
		}

		return loggedUser;
	}

	@PUT
	@Path("{id}/password")
	@Transactional
	public User updatePassword(User user) {
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		user.password = passwordEncryptor.encryptPassword(user.password);
		user.persist();
		return user;
	}
}
