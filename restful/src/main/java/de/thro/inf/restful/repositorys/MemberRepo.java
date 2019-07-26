package de.thro.inf.restful.repositorys;

import de.thro.inf.restful.models.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepo extends CrudRepository<Member, String> {
}
