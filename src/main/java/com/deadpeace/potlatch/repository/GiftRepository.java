package com.deadpeace.potlatch.repository;

import com.deadpeace.potlatch.security.User;
import com.deadpeace.potlatch.client.PotlatchSvcApi;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: DeadPeace
 * Date: 13.10.2014
 * Time: 12:01
 * To change this template use File | Settings | File Templates.
 */

@Repository
public interface GiftRepository extends CrudRepository<Gift,Long>
{
    public List<Gift> findByTitleContainingIgnoreCaseAndCreatorNot(@Param(PotlatchSvcApi.TITLE_PARAM) String title,@Param(PotlatchSvcApi.CREATOR_PARAM) User user);

    public List<Gift> findByCreatorOrderByDateDesc(@Param(PotlatchSvcApi.CREATOR_PARAM) User user);

    public List<Gift> findByCreatorNot(@Param(PotlatchSvcApi.CREATOR_PARAM) User user);

    public List<Gift> findByRecipientsOrderByDateDesc(@Param(PotlatchSvcApi.ID) User user);
}
