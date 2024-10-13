package com.myproject.autopartsestoresystem.controller;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

/**
 * @author Miroslav Kološnjaji
 */
public abstract class BaseIT {

    public static final String IDX_WITH_ARGS = "#{index} with [{arguments}]";
    private static final String SOURCE_PATH = "com.myproject.autopartsestoresystem.controller.BrandControllerIT#";
    public static final String GET_ADMIN_USER = SOURCE_PATH + "getStreamAdminUser";
    public static final String GET_ADMIN_AND_MODERATOR_USERS = SOURCE_PATH + "getStreamAdminUser";
    public static final String GET_MODERATOR_USER = SOURCE_PATH + "getStreamModeratorUser";
    public static final String GET_USER = SOURCE_PATH + "getStreamUser";
    public static final String GET_ALL_USERS = SOURCE_PATH + "getStreamAllUsers";


    public static Stream<Arguments> getStreamAdminUser(){
        return Stream.of(getAdmin());
    }

    public static Stream<Arguments> getStreamModeratorUser(){
        return Stream.of(getModerator());
    }

    public static Stream<Arguments> getStreamUser(){
        return Stream.of(getUser());
    }

    public static Stream<Arguments> getStreamAdminAndModeratorUsers(){
        return Stream.of(getAdmin(), getModerator());
    }

    public static Stream<Arguments> getStreamAllUsers(){
        return Stream.of(getAdmin(), getModerator(), getUser());

    }

    private static Arguments getAdmin(){
        return Arguments.of("admin", "admin");
    }

    private static Arguments getModerator(){
        return Arguments.of("moderator", "password");
    }

    private static Arguments getUser(){
        return Arguments.of("user", "password");
    }

}
