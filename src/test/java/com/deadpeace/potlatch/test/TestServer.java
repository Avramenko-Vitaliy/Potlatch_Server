package com.deadpeace.potlatch.test;

import com.deadpeace.potlatch.client.PotlatchSvcApi;
import com.deadpeace.potlatch.client.SecuredRestBuilder;
import com.deadpeace.potlatch.repository.Gift;
import org.junit.Test;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: DeadPeace
 * Date: 13.10.2014
 * Time: 13:27
 * To change this template use File | Settings | File Templates.
 */
public class TestServer
{
    private final String TEST_URL = "https://localhost:8443";
    private PotlatchSvcApi potlachService=new SecuredRestBuilder()
            .setLoginEndpoint(TEST_URL + PotlatchSvcApi.TOKEN_PATH)
            .setUsername("admin")
            .setPassword("pass")
            .setClientId("mobile")
            .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
            .setEndpoint(TEST_URL).setLogLevel(RestAdapter.LogLevel.FULL).build()
            .create(PotlatchSvcApi.class);

    @Test
    public void testGiftAdd()throws IOException
    {
        Gift gift=new Gift();
        gift.setTitle("this is test gift");
        gift.setDescription("I'm develop servlet potlatch");
        gift=potlachService.addGift(gift);
        assertTrue(gift.getId()>0);
    }

    @Test
    public void testGiftList()
    {
        Gift gift=new Gift();
        gift.setTitle("this is test gift");
        gift.setDescription("I'm develop servlet potlatch");
        gift=potlachService.addGift(gift);
        Collection<Gift> stored = potlachService.getGiftList();
        assertTrue(stored.contains(gift));
    }

    @Test
    public void testInvalidGift()
    {
        try
        {
            Gift gift=new Gift();
            gift.setTitle("this is test gift");
            gift.setDescription("I'm develop servlet potlatch");
            potlachService.addGift(gift);
            gift=potlachService.getGiftById(-1222);
        }
        catch(RetrofitError e)
        {
            assertEquals(404,e.getResponse().getStatus());
        }
    }

    @Test
    public void testGift()
    {
        Gift gift=new Gift();
        gift.setTitle("this is test gift");
        gift.setDescription("I'm develop servlet potlatch");
        gift=potlachService.addGift(gift);
        assertTrue(potlachService.getGiftById(gift.getId())!=null);
    }

    @Test
    public void testUpload()
    {
        assertEquals(potlachService.uploadFile(new TypedFile("image/jpg", new File("D:\\", "myphoto.jpg")),"gift1"), "Done!");
    }

    @Test
    public void testLoadPhoto()
    {
        try
        {
            File photo = new File("D:\\download.jpg");
            FileOutputStream output = new FileOutputStream(photo);
            org.apache.commons.io.IOUtils.write(((TypedByteArray)potlachService.loadImage("admin").getBody()).getBytes(), output);
        }
        catch(Exception e)
        {
            fail();
        }
    }

    @Test
    public void testGiftCreator()
    {
        assertNotNull(potlachService.findByCreator(1));
    }

    @Test
    public void testSearchByTitle()
    {
        assertTrue(potlachService.findByTitle("first").size()>0);
    }

    @Test
    public void testGettingGift()
    {
        assertNotNull(potlachService.findByGetting(1));
    }

    @Test
    public void testDelGift()
    {
        assertTrue(potlachService.delGift(2));
    }

    @Test
    public void testDelRecipients()
    {
        assertTrue(potlachService.delRecipients(2,3));
    }

    @Test
    public void testRecipients()
    {
        assertNotNull(potlachService.sendRecipients(3,1));
    }

    @Test
    public void testPreference()
    {
        potlachService.setPreference(1,"this is my preference");
    }
}
