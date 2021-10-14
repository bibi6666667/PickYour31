package bibi.demo.repository;

import bibi.demo.domain.Base;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaseRepository extends CrudRepository<Base, Long> {

    @Override
    Optional<Base> findById(Long id);

}
