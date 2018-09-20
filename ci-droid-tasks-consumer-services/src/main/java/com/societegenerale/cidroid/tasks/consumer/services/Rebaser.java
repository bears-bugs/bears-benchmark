package com.societegenerale.cidroid.tasks.consumer.services;

import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequest;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface Rebaser {

    Pair<PullRequest,List<GitCommit>> rebase(PullRequest pr) ;
}
