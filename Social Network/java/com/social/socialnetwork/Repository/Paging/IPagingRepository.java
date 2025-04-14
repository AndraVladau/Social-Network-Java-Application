package com.social.socialnetwork.Repository.Paging;

import com.social.socialnetwork.Domain.Entitate;
import com.social.socialnetwork.Repository.Repository;

public interface IPagingRepository<ID , E extends Entitate<ID>> extends Repository<ID, E> {

    Page<E> findAll(Pageable pageable);
}
