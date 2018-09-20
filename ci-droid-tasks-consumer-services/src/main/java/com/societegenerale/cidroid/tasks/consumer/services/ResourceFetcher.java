package com.societegenerale.cidroid.tasks.consumer.services;

import java.util.Optional;

public interface ResourceFetcher {

    /**
     * @param remoteResource
     * @return the resource content as a String. null if resource isn't found / doesn't exist.
     */
    Optional<String> fetch(String remoteResource);

}
