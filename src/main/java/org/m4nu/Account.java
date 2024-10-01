package org.m4nu;

import java.util.UUID;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Account extends PanacheEntityBase {
	@Id
	String id = UUID.randomUUID().toString();
	public String username;
	public String password;
	public String name;

	// public void setPassword(String password) {
	// 	ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
	// 	passwordEncryptor.setAlgorithm("SHA-1");
	// 	this.password = passwordEncryptor.encryptPassword(password);
	// }
}
