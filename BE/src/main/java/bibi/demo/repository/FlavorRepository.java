package bibi.demo.repository;

import bibi.demo.domain.flavor.Flavor;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlavorRepository extends CrudRepository<Flavor, Long> {

    @Override
    List<Flavor> findAll();

    List<Flavor> findByNameKRContaining(String nameKR);

    List<Flavor> findByNameENContainingIgnoreCase(String nameEN);

    List<Flavor> findAll(Sort sort);
}
