package org.m4nu;

import java.util.List;
import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Task extends PanacheEntityBase {
	@Id
	String id = UUID.randomUUID().toString();
	public String name;
	public Status status;
	public String description;
	public String creationDate;
	public String lastUpdate;
	public String userId;

	public static List<Task> getUserTasks(String id) {
		return list("userId", id);
	}
}
