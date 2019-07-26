package de.thro.inf.restful.repositorys;

import de.thro.inf.restful.models.Clothes;
import org.springframework.data.repository.CrudRepository;

public interface ClothesRepo extends CrudRepository<Clothes, Long> {
}
