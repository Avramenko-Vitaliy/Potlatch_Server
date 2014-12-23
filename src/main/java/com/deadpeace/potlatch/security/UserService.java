package com.deadpeace.potlatch.security;

import com.deadpeace.potlatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: DeadPeace
 * Date: 17.12.2014
 * Time: 9:39
 * To change this template use File | Settings | File Templates.
 */

@Service("userService")
public class UserService implements UserDetailsService
{
    @Autowired
    private UserRepository repository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return repository.findByUsername(username);
    }
}
