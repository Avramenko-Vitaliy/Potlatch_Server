package com.deadpeace.potlatch;

import com.deadpeace.potlatch.security.User;
import com.deadpeace.potlatch.client.PotlatchSvcApi;
import com.deadpeace.potlatch.repository.Gift;
import com.deadpeace.potlatch.repository.GiftRepository;
import com.deadpeace.potlatch.repository.UserRepository;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: DeadPeace
 * Date: 13.10.2014
 * Time: 12:00
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class PotlatchSvc
{
    @Autowired
    private GiftRepository gifts;

    @Autowired
    private UserRepository users;

    @RequestMapping(value = PotlatchSvcApi.USER_SVC_LOGIN,method = RequestMethod.GET)
    public @ResponseBody User getUser(Principal principal,HttpServletResponse response)throws UsernameNotFoundException
    {
        User user=users.findByUsername(principal.getName());
        if(null==user)
            throw new UsernameNotFoundException("User with this login "+principal.getName()+" not found!");
        return user;
    }

    @Transactional
    @RequestMapping(value = PotlatchSvcApi.GIFT_SVC_PATH,method =RequestMethod.POST)
    public @ResponseBody Gift addGift(@RequestBody Gift g,Principal principal) throws IOException
    {
        g.setCreator(users.findByUsername(principal.getName()));
        g.setDate(System.currentTimeMillis());
        return gifts.save(g);
    }

    @RequestMapping(value = PotlatchSvcApi.GIFT_SVC_PATH,method = RequestMethod.GET)
    public @ResponseBody List<Gift> getGiftList(Principal principal)
    {
        return Lists.newArrayList(gifts.findByCreatorOrderByDateDesc(users.findByUsername(principal.getName())));
    }

    @Transactional
    @RequestMapping(value = PotlatchSvcApi.GIFT_SVC_LIKE_OR_UNLIKE,method = RequestMethod.POST)
    public @ResponseBody Gift likeOrUnlike(@PathVariable(PotlatchSvcApi.ID)long id,Principal principal,HttpServletResponse response)throws IOException
    {
        Gift gift=gifts.findOne(id);
        if(gift!=null)
        {
            response.setStatus(HttpServletResponse.SC_OK);
            gift.setLikeOrUnlike(users.findByUsername(principal.getName()));
            return gifts.save(gift);
        }
        else
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @Transactional
    @RequestMapping(value = PotlatchSvcApi.GIFT_SVC_OBSCENE_OR_DECENT,method = RequestMethod.POST)
    public @ResponseBody Gift obsceneOrDecent(@PathVariable(PotlatchSvcApi.ID)long id,Principal principal,HttpServletResponse response)throws IOException
    {
        Gift gift=gifts.findOne(id);
        if(gift!=null)
        {
            response.setStatus(HttpServletResponse.SC_OK);
            gift.setObsceneOrDecent(users.findByUsername(principal.getName()));
            return gifts.save(gift);
        }
        else
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @RequestMapping(value = PotlatchSvcApi.GIFT_SVC_ID,method = RequestMethod.GET)
    public @ResponseBody Gift getGiftById(@PathVariable(PotlatchSvcApi.ID)long id,HttpServletResponse response)throws IOException
    {
        Gift gift=gifts.findOne(id);
        if(gift==null)
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return gift;
    }

    @RequestMapping(value = PotlatchSvcApi.GIFT_SVC_UPLOAD,method = RequestMethod.POST)
    public @ResponseBody String uploadFile(@RequestParam(value = "file")MultipartFile file,@RequestParam(value = "name") String name)
    {
        if(!file.isEmpty())
        {
            try
            {
                byte[] bytes=file.getBytes();
                BufferedOutputStream stream=new BufferedOutputStream(new FileOutputStream(new File(".\\images",name)));
                stream.write(bytes);
                stream.close();
                return "Done!";
            }
            catch(Exception e)
            {
                return e.getMessage();
            }
        }
        else
            return "File is empty!";
    }

    @RequestMapping(value = PotlatchSvcApi.LOAD_IMAGE,method = RequestMethod.GET)
    public @ResponseBody byte[] loadImage(@PathVariable("name") String name)throws IOException
    {
        File file=new File(".\\images",name+".jpg");
        return FileUtils.readFileToByteArray(file.exists()?file:new File(".\\images\\noimage.jpg"));
    }

    @RequestMapping(value=PotlatchSvcApi.GIFT_TITLE_SEARCH_PATH, method=RequestMethod.GET)
    public @ResponseBody List<Gift> findByTitle(@RequestParam(PotlatchSvcApi.TITLE_PARAM) String title,Principal principal)
    {
        return gifts.findByTitleContainingIgnoreCaseAndCreatorNot(title,users.findByUsername(principal.getName()));
    }

    @RequestMapping(value=PotlatchSvcApi.GIFT_CREATOR_PATH, method=RequestMethod.GET)
    public @ResponseBody List<Gift> findByCreator(@PathVariable(PotlatchSvcApi.ID) long id)
    {
        return gifts.findByCreatorOrderByDateDesc(users.findOne(id));
    }

    @RequestMapping(value=PotlatchSvcApi.GIFT_NOT_CREATOR, method=RequestMethod.GET)
    public @ResponseBody List<Gift> findByCreatorNot(@PathVariable(PotlatchSvcApi.ID) long id)
    {
        return gifts.findByCreatorNot(users.findOne(id));
    }

    @RequestMapping(value = PotlatchSvcApi.GIFT_GETTING_PATH,method = RequestMethod.GET)
    public @ResponseBody List<Gift> findByGetting(@PathVariable(PotlatchSvcApi.ID) long id)
    {
        return gifts.findByRecipientsOrderByDateDesc(users.findOne(id));
    }

    @Transactional
    @RequestMapping(value = PotlatchSvcApi.GIFT_SVC_DEL,method = RequestMethod.DELETE)
    public @ResponseBody boolean delGift(@PathVariable(PotlatchSvcApi.ID)long id)
    {
        try
        {
            new File(".\\images","gift_"+id+".jpg").delete();
            if(gifts.findOne(id)!=null)
                gifts.delete(id);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    @Transactional
    @RequestMapping(value = PotlatchSvcApi.GIFT_SVC_DEL_RECIPIENTS,method = RequestMethod.DELETE)
    public @ResponseBody boolean delRecipients(@RequestParam(PotlatchSvcApi.USER_ID_PARAM)long user_id,@RequestParam(PotlatchSvcApi.GIFT_ID_PARAM)long gift_id)
    {
        Gift gift=gifts.findOne(gift_id);
        if(gift!=null)
        {
            gift.getRecipients().remove(users.findOne(user_id));
            gifts.save(gift);
        }
        return true;
    }

    @Transactional
    @RequestMapping(value = PotlatchSvcApi.GIFT_SVC_SEND_RECIPIENTS,method = RequestMethod.POST)
    public @ResponseBody Gift sendRecipients(@PathVariable(PotlatchSvcApi.ID)long id,@PathVariable(PotlatchSvcApi.USER_ID_PARAM)long u_id,HttpServletResponse response)throws IOException
    {
        Gift gift=gifts.findOne(id);
        User user=users.findOne(u_id);
        if(user==null||gift==null)
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        else
        {
            gift.getRecipients().add(user);
            response.setStatus(HttpServletResponse.SC_OK);
            return gifts.save(gift);
        }
    }

    @Transactional
    @RequestMapping(value = PotlatchSvcApi.USER_SVC_PREFERENCE,method = RequestMethod.POST)
    public @ResponseBody User setPreference(@PathVariable(PotlatchSvcApi.ID)long id,@RequestParam(PotlatchSvcApi.USER_PREFER_PARAM)String preference)throws UsernameNotFoundException
    {
        User user=users.findOne(id);
        if(user!=null)
        {
            user.setPreference(preference);
            user=users.save(user);
        }
        else
            throw new UsernameNotFoundException("User not found!");
        return user;
    }
}
