package course.springdata.mapping.repository;

import course.springdata.mapping.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {


    Optional<User> findByEmailAndPassword(String email,String password);

    boolean existsByEmail(String email);


    User findByEmail(String email);
}
