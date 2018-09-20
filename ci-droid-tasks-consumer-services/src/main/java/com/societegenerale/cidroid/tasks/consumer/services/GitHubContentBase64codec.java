package com.societegenerale.cidroid.tasks.consumer.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

@Slf4j
public class GitHubContentBase64codec {

    private GitHubContentBase64codec() {
    }

    public static String encode(String input) {

        try {
            return Base64.encodeBase64String(input.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("couldn't encode provided data {}", input, e);
        }

        return "";

    }

    public static String decode(String input) {

        try {
            return new String(Base64.decodeBase64(input.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            log.error("couldn't decode provided data {}", input, e);
        }
        return "";
    }

}
