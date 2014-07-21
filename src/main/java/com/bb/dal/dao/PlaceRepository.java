package com.bb.dal.dao;

import com.bb.dal.entity.Place;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: belozovs
 * Date: 7/14/14
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */

public interface PlaceRepository extends PagingAndSortingRepository<Place, Long> {

    Place findByAlias(String alias);

    List<Place> findByNameLike(String nameLike);

    @Query(value="{'alias' :?0}", fields = "{ 'name' : 1, 'notes' : 1}")
    Place findNameAndNotesByAlias(String aliasParam);

    @Query(value="{'alias' :{$regex : ?0, $options: 'i'}}", fields = "{ 'name' : 1, 'notes' : 1}")
    List<Place> findNameAndNotesByAliasLikeCaseInsensitive(String aliasParam);

    @Query(value="{'alias' :{$regex : ?0}}", fields = "{ 'name' : 1, 'notes' : 1}")
    List<Place> findNameAndNotesByAliasLike(String aliasParam);

}
