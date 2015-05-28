package com.deadpeace.potlatch.repository;

import com.deadpeace.potlatch.security.User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Objects;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: DeadPeace
 * Date: 13.10.2014
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */

@Entity
@JsonAutoDetect
public class Gift implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;
    private String description;

    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date date;

    @ManyToMany
    @JoinTable(name = "obscene")
    private List<User> obscene;

    @ManyToMany
    @JoinTable(name = "liked")
    private List<User> liked;

    @ManyToMany
    @JoinTable(name = "recipients")
    private List<User> recipients;

    @ManyToOne
    @JoinColumn(name = "id_creator",nullable = false)
    private User creator;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id=id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title=title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description=description;
    }

    public User getCreator()
    {
        return creator;
    }

    public void setCreator(User creator)
    {
        this.creator=creator;
    }

    public List<User> getLiked()
    {
        return liked;
    }

    public void setLiked(List<User> liked)
    {
        this.liked=liked;
    }

    public List<User> getObscene()
    {
        return obscene;
    }

    public void setObscene(List<User> obscene)
    {
        this.obscene=obscene;
    }

    public List<User> getRecipients()
    {
        return recipients;
    }

    public void setRecipients(List<User> recipients)
    {
        this.recipients=recipients;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date=date;
    }

    public synchronized void setLikeOrUnlike(User user)
    {
        if(liked.contains(user))
            liked.remove(user);
        else
            liked.add(user);
    }

    public synchronized void setObsceneOrDecent(User user)
    {
        if(obscene.contains(user))
            obscene.remove(user);
        else
            obscene.add(user);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Gift)
        {
            Gift other=(Gift) obj;
            return Objects.equal(id, other.id);
        }
        else
            return false;
    }
}
