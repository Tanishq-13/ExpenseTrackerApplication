package com.example.demo.Serializer;

import com.example.demo.entities.UserInfo;
import com.example.demo.eventProducer.UserInfoEvent;
import com.example.demo.service.UserDetailsServiceImplementation;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.SerializationException;

import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;


public class UserInfoSerializer implements Serializer<UserInfoEvent> {

    private static final Logger log = LoggerFactory.getLogger(UserInfoSerializer.class);


    //userInfo ko string me convert kiya
//    {
//        "first_name":"abs"
//            "lastName":"efg"
//            //ye object hai isko pehle string me bana dega fir bytes me bana dega
//    }


    @Override
    public byte[] serialize(String s, UserInfoEvent userInfo) {
        byte[] bytes = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.debug("Serializing user info");
            bytes=objectMapper.writeValueAsString(userInfo).getBytes();

        }catch (Exception e){
//            log.debug("serialize error",e);
            e.printStackTrace();
        }
        return bytes;
    }
    //object mapper for going from one object to another yidto->userinfo

}
