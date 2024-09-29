package org.m4nu;

import java.util.UUID;

import org.jasypt.util.password.StrongPasswordEncryptor;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User extends PanacheEntityBase {
	@Id
	UUID id = UUID.randomUUID();
	public String username;
	public String password;
	public String name;

	public void setPassword(String password) {
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		this.password = passwordEncryptor.encryptPassword(password);
	}
}
