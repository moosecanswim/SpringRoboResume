package me.moosecanswim.springroboresume.repositories;
import me.moosecanswim.springroboresume.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role,Long> {
    Role findByRole(String role);
}
