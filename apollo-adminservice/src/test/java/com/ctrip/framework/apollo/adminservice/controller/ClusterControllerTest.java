package com.ctrip.framework.apollo.adminservice.controller;

import com.ctrip.framework.apollo.biz.entity.Cluster;
import com.ctrip.framework.apollo.biz.service.ClusterService;
import com.ctrip.framework.apollo.common.exception.BadRequestException;
import com.ctrip.framework.apollo.core.ConfigConsts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ClusterControllerTest{

    ClusterController clusterController=new ClusterController();
    @Mock
    ClusterService clusterService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(clusterController, "clusterService", clusterService);
    }


    @Test(expected = BadRequestException.class)
    public void testDeleteDefaultFail() {
        Cluster cluster=new Cluster();
        cluster.setName(ConfigConsts.CLUSTER_NAME_DEFAULT);
        when(clusterService.findOne(any(String.class), any(String.class))).thenReturn(cluster);
        clusterController.delete("1","2","d");
    }

    @Test()
    public void testDeleteSuccess() {
        Cluster cluster=new Cluster();
        when(clusterService.findOne(any(String.class), any(String.class))).thenReturn(cluster);
        clusterController.delete("1","2","d");
    }
}