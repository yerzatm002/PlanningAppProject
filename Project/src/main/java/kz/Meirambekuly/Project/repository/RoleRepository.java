package kz.Meirambekuly.Project.repository;

import kz.Meirambekuly.Project.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
