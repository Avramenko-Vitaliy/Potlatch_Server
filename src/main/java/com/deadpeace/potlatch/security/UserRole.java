package com.deadpeace.potlatch.security;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created with IntelliJ IDEA.
 * User: DeadPeace
 * Date: 17.12.2014
 * Time: 8:58
 * To change this template use File | Settings | File Templates.
 */

@Entity
public class UserRole implements GrantedAuthority
{
    @Id
    @GeneratedValue
    private long id;

    private String authority;

    @Override
    public String getAuthority()
    {
        return authority;
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof String)
            return obj.equals(this.authority);
        if(obj instanceof GrantedAuthority)
        {
            GrantedAuthority attr=(GrantedAuthority) obj;

            return this.authority.equals(attr.getAuthority());
        }
        return false;
    }

    public int hashCode() {
        return this.authority.hashCode();
    }

    public String toString() {
        return this.authority;
    }
}
