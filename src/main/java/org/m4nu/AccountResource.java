package org.m4nu;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.m4nu.security.jwt.GenerateToken;

import io.quarkus.security.Authenticated;
import io.quarkus.security.UnauthorizedException;
import jakarta.annotation.security.PermitAll;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

@Path("/user")
public class AccountResource {

	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
	@POST
	@Path("/register")
	@PermitAll
	@Transactional
	public Account createAccount(Account user) {
		user.password = passwordEncryptor.encryptPassword(user.password);
		user.persist();
		return user;
	}

	@POST
	@Path("/login")
	@PermitAll
	@Transactional
	public String loginAccount(Account user) {
		Account loggedAccount = Account.find("username = ?1", user.username).firstResult();

		if (loggedAccount == null) {
			throw new NotFoundException();
		}

		if (!passwordEncryptor.checkPassword(user.password, loggedAccount.password)) {
			throw new UnauthorizedException();
		}

		return GenerateToken.generateToken(loggedAccount.id, loggedAccount.username);
	}
	@PUT
	@Path("{id}/password")
	@Authenticated
	@Transactional
	public Account updatePassword(Account user) {
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		user.password = passwordEncryptor.encryptPassword(user.password);
		user.persist();
		return user;
	}
}
