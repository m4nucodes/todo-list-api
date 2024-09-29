package org.m4nu;

import java.util.List;
import java.util.UUID;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

@Path("/tasks")
public class TaskResource {
	@GET
	public List<Task> getTasks() {
		return Task.listAll();
	}

	@GET
	@Path("/{id}")
	public Task getTaskById(UUID id) {
		if (Task.findById(id) == null) {
			throw new NotFoundException();
		}
		return Task.findById(id);
	}

	@POST
	@Transactional
	public Task createTask(Task task) {
		task.persist();
		return task;
	}

	@PUT
	@Path("/{id}")
	@Transactional
	public Task updateTask(UUID id, Task task) {
		Task existingTask = Task.findById(id);

		if (existingTask == null) {
			throw new NotFoundException();
		}

		existingTask.name = task.name;
		existingTask.status = task.status;
		existingTask.description = task.description;
		existingTask.lastUpdate = task.lastUpdate;
		existingTask.userId = task.userId;

		return existingTask;
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public void deleteTask(UUID id) {
		if (Task.findById(id) == null) {
			throw new NotFoundException();
		}

		Task.deleteById(id);
	}
}
