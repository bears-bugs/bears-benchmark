package com.societegenerale.cidroid.tasks.consumer.services.model.github;

import lombok.Getter;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

public enum PRmergeableStatus {

    MERGEABLE(true),
    NOT_MERGEABLE(false),
    UNKNOWN(null);

    @Getter
    private Boolean value;

    public final static Map<Boolean, PRmergeableStatus> mapping =
            stream(PRmergeableStatus.values()).collect(toMap(leg -> leg.value, leg -> leg));

    PRmergeableStatus(Boolean value) {
        this.value=value;
    }


}
