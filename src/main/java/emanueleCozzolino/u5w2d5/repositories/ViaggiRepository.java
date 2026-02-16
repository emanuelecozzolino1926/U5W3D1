package emanueleCozzolino.u5w2d5.repositories;

import emanueleCozzolino.u5w2d5.entities.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ViaggiRepository extends JpaRepository<Viaggio, UUID> {
}
