package com.deadpeace.potlatch.repository;

import com.deadpeace.potlatch.security.User;
import com.deadpeace.potlatch.client.PotlatchSvcApi;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: DeadPeace
 * Date: 23.10.2014
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */

@Repository
public interface UserRepository extends CrudRepository<User,Long>
{
    public User findByUsername(@Param(PotlatchSvcApi.USER_NAME_PARAM) String username);
}
