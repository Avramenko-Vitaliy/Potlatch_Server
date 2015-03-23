package com.deadpeace.potlatch.client;

import com.deadpeace.potlatch.security.User;
import com.deadpeace.potlatch.repository.Gift;
import retrofit.client.Response;
import retrofit.http.*;
import retrofit.mime.TypedFile;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: DeadPeace
 * Date: 13.10.2014
 * Time: 12:05
 * To change this template use File | Settings | File Templates.
 */
public interface PotlatchSvcApi
{
    public static final String RES_JSON=".json";

    public static final String ID="id";
    public static final String FILE="file";
    public static final String TITLE_PARAM="title";
    public static final String CREATOR_PARAM="creator";
    public static final String NAME_PARAM="name";
    public static final String USER_NAME_PARAM="username";
    public static final String PASSWORD_PARAM="password";
    public static final String GIFT_ID_PARAM="gift_id";
    public static final String USER_ID_PARAM="user_id";
    public static final String USER_PREFER_PARAM="preference";

    public static final String GIFT_SVC_PATH="/gift";
    public static final String GIFT_SVC_ID=GIFT_SVC_PATH+"/{"+ID+"}";
    public static final String GIFT_SVC_LIKE_OR_UNLIKE=GIFT_SVC_ID+"/like_or_unlike";
    public static final String GIFT_SVC_OBSCENE_OR_DECENT=GIFT_SVC_ID+"/obscene_or_decent";
    public static final String GIFT_SVC_UPLOAD=GIFT_SVC_PATH+"/upload";
    public static final String USER_SVC_LOGIN="/user";
    public static final String SVC_LOGIN="/login";
    public static final String SVC_LOGOUT="/logout";
    public static final String LOAD_IMAGE="/image/{"+NAME_PARAM+"}";
    public static final String GIFT_TITLE_SEARCH_PATH = GIFT_SVC_PATH + "/search/findByTitle";
    public static final String GIFT_CREATOR_PATH="/user/{"+ID+"}"+GIFT_SVC_PATH;
    public static final String GIFT_NOT_CREATOR="/not_user/{"+ID+"}"+GIFT_SVC_PATH;
    public static final String GIFT_GETTING_PATH=GIFT_SVC_ID+"/getting";
    public static final String GIFT_SVC_DEL=GIFT_SVC_ID+"/del";
    public static final String GIFT_SVC_DEL_RECIPIENTS="/delRecipients";
    public static final String GIFT_SVC_SEND_RECIPIENTS=GIFT_SVC_ID+"/recipients/user/{"+USER_ID_PARAM+"}";
    public static final String USER_SVC_PREFERENCE="/user/{"+ID+"}/setPreference";

    //Дабавление нового подарка
    @POST(GIFT_SVC_PATH+RES_JSON)
    public Gift addGift(@Body Gift g);

    //Получение списка всех подарков
    @GET(GIFT_SVC_PATH+RES_JSON)
    public List<Gift> getGiftList();

    //отменить отметку что подарок понравился
    @POST(GIFT_SVC_LIKE_OR_UNLIKE+RES_JSON)
    public Gift likeOrUnlike(@Path(ID)long id);

    @POST(GIFT_SVC_OBSCENE_OR_DECENT+RES_JSON)
    public Gift obsceneOrDecent(@Path(ID)long id);

    //получение подарка по ID
    //если подарок не найден возвращаем ошибку 404
    @GET(GIFT_SVC_ID+RES_JSON)
    public Gift getGiftById(@Path(ID)long id);

    //Прототип загрузки файла на сервер
    @Multipart
    @POST(GIFT_SVC_UPLOAD+RES_JSON)
    public String uploadFile(@Part(FILE) TypedFile photo,@Part(NAME_PARAM)String name);

    //Прототип получения файла на клиентское приложение
    @GET(LOAD_IMAGE+RES_JSON)
    public Response loadImage(@Path(NAME_PARAM) String name);

    //поиск подарков по названию
    @GET(GIFT_TITLE_SEARCH_PATH+RES_JSON)
    public List<Gift> findByTitle(@Query(TITLE_PARAM) String title);

    //получение имени пользователя
    @GET(USER_SVC_LOGIN+RES_JSON)
    public User getUser();

    @GET(GIFT_CREATOR_PATH+RES_JSON)
    public List<Gift> findByCreator(@Path(ID) long id);

    @GET(GIFT_NOT_CREATOR+RES_JSON)
    public List<Gift> findByCreatorNot(@Path(ID) long id);

    @GET(GIFT_GETTING_PATH+RES_JSON)
    public List<Gift> findByGetting(@Path(ID) long id);

    @DELETE(GIFT_SVC_DEL+RES_JSON)
    public boolean delGift(@Path(ID)long id);

    @DELETE(GIFT_SVC_DEL_RECIPIENTS+RES_JSON)
    public boolean delRecipients(@Query(USER_ID_PARAM)long user_id,@Query(GIFT_ID_PARAM)long gift_id);

    @POST(GIFT_SVC_SEND_RECIPIENTS+RES_JSON)
    public Gift sendRecipients(@Path(ID)long id,@Path(USER_ID_PARAM)long u_id);

    @POST(USER_SVC_PREFERENCE+RES_JSON)
    public User setPreference(@Path(ID)long id,@Query(USER_PREFER_PARAM)String preference);

    @FormUrlEncoded
    @POST(SVC_LOGIN)
    public Void login(@Field(USER_NAME_PARAM) String username, @Field(PASSWORD_PARAM) String pass);

    @GET(SVC_LOGOUT)
    public Void logout();
}
