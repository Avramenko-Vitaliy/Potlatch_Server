package com.deadpeace.potlatch.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: DeadPeace
 * Date: 17.12.2014
 * Time: 8:51
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class User implements UserDetails
{
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "authority")
    private Collection<UserRole> authorities;

    private boolean locked=false;
    private boolean enabled=true;
    private boolean credentials_expired=false;
    private boolean account_expired=false;

    private String preference;

    @Override
    public Collection<UserRole> getAuthorities()
    {
        return authorities;
    }

    public String getPreference()
    {
        return preference;
    }

    public void setPreference(String preference)
    {
        this.preference=preference;
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return !account_expired;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return !credentials_expired;
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }
}
