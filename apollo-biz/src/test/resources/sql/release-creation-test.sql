INSERT INTO `app` ( `AppId`, `Name`, `OrgId`, `OrgName`, `OwnerName`, `OwnerEmail`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES('test', 'test0620-06', 'default', 'default', 'default', 'default', 0, 'default', 'default');

/* normal namespace*/
INSERT INTO `cluster` ( `Name`, `AppId`, `ParentClusterId`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`) VALUES ( 'only-master', 'test', 0, 0, 'default', 'default');
INSERT INTO `namespace` (ID, `AppId`, `ClusterName`, `NamespaceName`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(100, 'test', 'only-master', 'application', 0, 'apollo', 'apollo');
INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(100, 'k1', 'v1', '', 'apollo', 'apollo');
INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(100, 'k2', 'v2', '', 'apollo', 'apollo');
INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(100, 'k3', 'v3', '', 'apollo', 'apollo');


/* namespace has branch. master has items but branch has not item*/
INSERT INTO `cluster` (ID, `Name`, `AppId`, `ParentClusterId`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`) VALUES (101, 'default1', 'test', 0, 0, 'default', 'default');
INSERT INTO `cluster` (ID, `Name`, `AppId`, `ParentClusterId`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(102, 'child-cluster1', 'test', 101, 0, 'default', 'default');

INSERT INTO `namespace` (ID, `AppId`, `ClusterName`, `NamespaceName`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(101, 'test', 'default1', 'application', 0, 'apollo', 'apollo');
INSERT INTO `namespace` (ID, `AppId`, `ClusterName`, `NamespaceName`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(102, 'test', 'child-cluster1', 'application', 0, 'apollo', 'apollo');

INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(101, 'k1', 'v1', '', 'apollo', 'apollo');
INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(101, 'k2', 'v2', '', 'apollo', 'apollo');
INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(101, 'k3', 'v3', '', 'apollo', 'apollo');

INSERT INTO `grayreleaserule` (`AppId`, `ClusterName`, `NamespaceName`, `BranchName`, `Rules`, `ReleaseId`, `BranchStatus`)VALUES ('test', 'default1', 'application', 'child-cluster1', '[]', 1155, 1);



/* namespace has branch. master has items and branch has item*/
INSERT INTO `cluster` (ID, `Name`, `AppId`, `ParentClusterId`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`) VALUES (103, 'default2', 'test', 0, 0, 'default', 'default');
INSERT INTO `cluster` (ID, `Name`, `AppId`, `ParentClusterId`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(104, 'child-cluster2', 'test', 103, 0, 'default', 'default');

INSERT INTO `namespace` (ID, `AppId`, `ClusterName`, `NamespaceName`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(103, 'test', 'default2', 'application', 0, 'apollo', 'apollo');
INSERT INTO `namespace` (ID, `AppId`, `ClusterName`, `NamespaceName`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(104, 'test', 'child-cluster2', 'application', 0, 'apollo', 'apollo');

INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(103, 'k1', 'v1', '', 'apollo', 'apollo');
INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(103, 'k2', 'v2', '', 'apollo', 'apollo');
INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(103, 'k3', 'v3', '', 'apollo', 'apollo');
INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(104, 'k1', 'v1-1', '', 'apollo', 'apollo');

INSERT INTO `grayreleaserule` (`AppId`, `ClusterName`, `NamespaceName`, `BranchName`, `Rules`, `ReleaseId`, `BranchStatus`)VALUES ('test', 'default2', 'application', 'child-cluster2', '[]', 1155, 1);

/* namespace has branch. master has items and branch has cover item */
INSERT INTO `cluster` (ID, `Name`, `AppId`, `ParentClusterId`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`) VALUES (105, 'default3', 'test', 0, 0, 'default', 'default');
INSERT INTO `cluster` (ID, `Name`, `AppId`, `ParentClusterId`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(106, 'child-cluster3', 'test', 105, 0, 'default', 'default');

INSERT INTO `namespace` (ID, `AppId`, `ClusterName`, `NamespaceName`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(105, 'test', 'default3', 'application', 0, 'apollo', 'apollo');
INSERT INTO `namespace` (ID, `AppId`, `ClusterName`, `NamespaceName`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(106, 'test', 'child-cluster3', 'application', 0, 'apollo', 'apollo');

INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(105, 'k1', 'v1', '', 'apollo', 'apollo');
INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(105, 'k2', 'v2-2', '', 'apollo', 'apollo');
INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(106, 'k1', 'v1-2', '', 'apollo', 'apollo');

INSERT INTO `release` (`Id`, `ReleaseKey`, `Name`, `Comment`, `AppId`, `ClusterName`, `NamespaceName`, `Configurations`, `IsAbandoned`)VALUES(1, '20160823102253-fc0071ddf9fd3260', '20160823101703-release', '', 'test', 'default3', 'application', '{"k1":"v1","k2":"v2","k3":"v3"}', 0);
INSERT INTO `release` (`Id`, `ReleaseKey`, `Name`, `Comment`, `AppId`, `ClusterName`, `NamespaceName`, `Configurations`, `IsAbandoned`)VALUES(2, '20160823102253-fc0071ddf9fd3260', '20160823101703-release', '', 'test', 'child-cluster3', 'application', '{"k1":"v1-1","k2":"v2","k3":"v3"}', 0);

INSERT INTO `grayreleaserule` (`AppId`, `ClusterName`, `NamespaceName`, `BranchName`, `Rules`, `ReleaseId`, `BranchStatus`)VALUES ('test', 'default3', 'application', 'child-cluster3', '[]', 1155, 1);

/*publish branch at first time */
INSERT INTO `cluster` (ID, `Name`, `AppId`, `ParentClusterId`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`) VALUES (107, 'default4', 'test', 0, 0, 'default', 'default');
INSERT INTO `cluster` (ID, `Name`, `AppId`, `ParentClusterId`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(108, 'child-cluster4', 'test', 107, 0, 'default', 'default');

INSERT INTO `namespace` (ID, `AppId`, `ClusterName`, `NamespaceName`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(107, 'test', 'default4', 'application', 0, 'apollo', 'apollo');
INSERT INTO `namespace` (ID, `AppId`, `ClusterName`, `NamespaceName`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(108, 'test', 'child-cluster4', 'application', 0, 'apollo', 'apollo');

INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(107, 'k1', 'v1', '', 'apollo', 'apollo');
INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(107, 'k2', 'v2-2', '', 'apollo', 'apollo');
INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(108, 'k1', 'v1-2', '', 'apollo', 'apollo');
INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(108, 'k4', 'v4', '', 'apollo', 'apollo');

INSERT INTO `release` (`Id`, `ReleaseKey`, `Name`, `Comment`, `AppId`, `ClusterName`, `NamespaceName`, `Configurations`, `IsAbandoned`)VALUES(3, '20160823102253-fc0071ddf9fd3260', '20160823101703-release', '', 'test', 'default4', 'application', '{"k1":"v1","k2":"v2","k3":"v3"}', 0);

INSERT INTO `grayreleaserule` (`AppId`, `ClusterName`, `NamespaceName`, `BranchName`, `Rules`, `ReleaseId`, `BranchStatus`)VALUES ('test', 'default4', 'application', 'child-cluster4', '[]', 1155, 1);

/*publish branch*/
INSERT INTO `cluster` (ID, `Name`, `AppId`, `ParentClusterId`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`) VALUES (109, 'default5', 'test', 0, 0, 'default', 'default');
INSERT INTO `cluster` (ID, `Name`, `AppId`, `ParentClusterId`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(1010, 'child-cluster5', 'test', 109, 0, 'default', 'default');

INSERT INTO `namespace` (ID, `AppId`, `ClusterName`, `NamespaceName`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(109, 'test', 'default5', 'application', 0, 'apollo', 'apollo');
INSERT INTO `namespace` (ID, `AppId`, `ClusterName`, `NamespaceName`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(1010, 'test', 'child-cluster5', 'application', 0, 'apollo', 'apollo');

INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(109, 'k1', 'v1', '', 'apollo', 'apollo');
INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(109, 'k2', 'v2-2', '', 'apollo', 'apollo');
INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(1010, 'k1', 'v1-2', '', 'apollo', 'apollo');
INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(1010, 'k4', 'v4', '', 'apollo', 'apollo');
INSERT INTO `item` (`NamespaceId`, `Key`, `Value`, `Comment`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(1010, 'k6', 'v6', '', 'apollo', 'apollo');

INSERT INTO `release` (`Id`, `ReleaseKey`, `Name`, `Comment`, `AppId`, `ClusterName`, `NamespaceName`, `Configurations`, `IsAbandoned`)VALUES(4, '20160823102253-fc0071ddf9fd3260', '20160823101703-release', '', 'test', 'default5', 'application', '{"k1":"v1","k2":"v2","k3":"v3"}', 0);
INSERT INTO `release` (`Id`, `ReleaseKey`, `Name`, `Comment`, `AppId`, `ClusterName`, `NamespaceName`, `Configurations`, `IsAbandoned`)VALUES(5, '20160823102253-fc0071ddf9fd3260', '20160823101703-release', '', 'test', 'child-cluster5', 'application', '{"k1":"v1-1","k2":"v2","k3":"v3","k4":"v4","k5":"v5"}', 0);

INSERT INTO `grayreleaserule` (`AppId`, `ClusterName`, `NamespaceName`, `BranchName`, `Rules`, `ReleaseId`, `BranchStatus`)VALUES ('test', 'default5', 'application', 'child-cluster5', '[]', 1155, 1);

/* rollback */
INSERT INTO `cluster` (ID, `Name`, `AppId`, `ParentClusterId`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`) VALUES (1011, 'default6', 'test', 0, 0, 'default', 'default');
INSERT INTO `cluster` (ID, `Name`, `AppId`, `ParentClusterId`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(1012, 'child-cluster6', 'test', 1011, 0, 'default', 'default');

INSERT INTO `namespace` (ID, `AppId`, `ClusterName`, `NamespaceName`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(1011, 'test', 'default6', 'application', 0, 'apollo', 'apollo');
INSERT INTO `namespace` (ID, `AppId`, `ClusterName`, `NamespaceName`, `IsDeleted`, `DataChange_CreatedBy`, `DataChange_LastModifiedBy`)VALUES(1012, 'test', 'child-cluster6', 'application', 0, 'apollo', 'apollo');

INSERT INTO `release` (`Id`, `ReleaseKey`, `Name`, `Comment`, `AppId`, `ClusterName`, `NamespaceName`, `Configurations`, `IsAbandoned`)VALUES(6, '20160823102253-fc0071ddf9fd3260', '20160823101703-release', '', 'test', 'default6', 'application', '{"k1":"v1-1","k2":"v2-1","k3":"v3"}', 0);
INSERT INTO `release` (`Id`, `ReleaseKey`, `Name`, `Comment`, `AppId`, `ClusterName`, `NamespaceName`, `Configurations`, `IsAbandoned`)VALUES(7, '20160823102253-fc0071ddf9fd3260', '20160823101703-release', '', 'test', 'default6', 'application', '{"k1":"v1","k2":"v2"}', 0);
INSERT INTO `release` (`Id`, `ReleaseKey`, `Name`, `Comment`, `AppId`, `ClusterName`, `NamespaceName`, `Configurations`, `IsAbandoned`)VALUES(8, '20160823102253-fc0071ddf9fd3260', '20160823101703-release', '', 'test', 'child-cluster6', 'application', '{"k1":"v1-2","k2":"v2-1","k3":"v3"}', 0);
