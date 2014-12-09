/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package com.deadpeace.potlatch.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
public class User implements UserDetails
{
    @Id
    @GeneratedValue
    private long id;

    public User()
    {
        authorities_=null;
        password=null;
        username=null;
    }

    public static UserDetails create(String username, String password,String... authorities)
    {
        return new User(username, password, authorities);
    }

    @Transient
    @JsonIgnore
    private final Collection<GrantedAuthority> authorities_;

    @Transient
    @JsonIgnore
    private final String password;

    @ManyToMany
    @JoinTable(name = "friends")
    private List<User> friends;

    @Column(unique = true)
    private String username;

    private String preference;

    @SuppressWarnings("unchecked")
    private User(String username, String password)
    {
        this(username, password, Collections.EMPTY_LIST);
    }

    private User(String username, String password,String... authorities)
    {
        this.username=username;
        this.password=password;
        authorities_=AuthorityUtils.createAuthorityList(authorities);
    }

    private User(String username, String password,Collection<GrantedAuthority> authorities)
    {
        super();
        this.username=username;
        this.password=password;
        authorities_=authorities;
    }

    public Collection<GrantedAuthority> getAuthorities()
    {
        return authorities_;
    }

    public String getPassword()
    {
        return password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username=username;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id=id;
    }

    public List<User> getFriends()
    {
        return friends;
    }

    public void setFriends(List<User> friends)
    {
        this.friends=friends;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    public String getPreference()
    {
        return preference;
    }

    public void setPreference(String preference)
    {
        this.preference=preference;
    }
}
