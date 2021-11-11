package kz.Meirambekuly.Project.repository;

import kz.Meirambekuly.Project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User getByUsernameAndPassword(String username, String password);
}
