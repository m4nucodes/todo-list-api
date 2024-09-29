package org.m4nu;

import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Task extends PanacheEntityBase {
	@Id
	UUID id = UUID.randomUUID();
	public String name;
	public Status status;
	public String description;
	public String creationDate;
	public String lastUpdate;
	public String userId;
}
