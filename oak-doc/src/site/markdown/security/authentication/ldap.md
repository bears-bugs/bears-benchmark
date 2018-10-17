<!--
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
-->

LDAP Integration
--------------------------------------------------------------------------------

Oak comes with a default implementation of an LDAP identity provider that allows
perform external authentication against an existing LDAP in combination
with user synchronization.

See section [External Login Module and User Synchronization](externalloginmodule.html)
for a general overview of the `ExternalLoginModule` and how it can be used
in combination with custom identity providers and synchronization handlers.

### Default Setup

Out of the box Oak comes with the following LDAP integration setup:

- `LdapIdentityProvider`: LDAP specific implementation of the [ExternalIdentityProvider] interface.
- `DefaultSyncHandler`: Default implementation of the [SyncHandler] interface.
- `ExternalLoginModule`: Login module implementation that allows for third party authentication as specified by the configured identity provider(s).

#### LDAP Identity Provider

The [LdapIdentityProvider] is a service implementing the [ExternalIdentityProvider] interface.

_todo combining multiple ldap sources todo_

##### Configuration

The LDAP IPDs are configured through the [org.apache.jackrabbit.oak.security.authentication.ldap.impl.LdapProviderConfig]
which is populated either via OSGi or during manual [Repository Construction](../construct.html).

| Name                         | Property                | Description                              |
|------------------------------|-------------------------|------------------------------------------|
| LDAP Provider Name           | `provider.name`         | Name of this LDAP provider configuration. This is used to reference this provider by the login modules. |
| Bind DN                      | `bind.dn`               | DN of the user for authentication. Leave empty for anonymous bind. |
| Bind Password                | `bind.password`         | Password of the user for authentication. |
| LDAP Server Hostname         | `host.name`             | Hostname of the LDAP server              |
| Disable certificate checking | `host.noCertCheck`      | Indicates if server certificate validation should be disabled. |
| LDAP Server Port             | `host.port`             | Port of the LDAP server                  |
| Use SSL                      | `host.ssl`              | Indicates if an SSL (LDAPs) connection should be used. |
| Use TLS                      | `host.tls`              | Indicates if TLS should be started on connections. |
| Search Timeout               | `searchTimeout`         | Time in until a search times out (eg: '1s' or '1m 30s'). |
| User base DN                 | `user.baseDN`           | The base DN for user searches.           |
| User extra filter            | `user.extraFilter`      | Extra LDAP filter to use when searching for users. The final filter is formatted like: `(&(<idAttr>=<userId>)(objectclass=<objectclass>)<extraFilter>)` |
| User id attribute            | `user.idAttribute`      | Name of the attribute that contains the user id. |
| User DN paths                | `user.makeDnPath`       | Controls if the DN should be used for calculating a portion of the intermediate path. |
| User object classes          | `user.objectclass`      | The list of object classes an user entry must contain. |
| Group base DN                | `group.baseDN`          | The base DN for group searches.          |
| Group extra filter           | `group.extraFilter`     | Extra LDAP filter to use when searching for groups. The final filter is formatted like: `(&(<nameAttr>=<groupName>)(objectclass=<objectclass>)<extraFilter>)` |
| Group DN paths               | `group.makeDnPath`      | Controls if the DN should be used for calculating a portion of the intermediate path. |
| Group member attribute       | `group.memberAttribute` | Group attribute that contains the member(s) of a group. |
| Group name attribute         | `group.nameAttribute`   | Name of the attribute that contains the group name. |
| Group object classes         | `group.objectclass`     | The list of object classes a group entry must contain. |
| | | |

#### SyncHandler and External Login Module

See [External Login Module and User Synchronization](externalloginmodule.html) for
details about the external login module and configuration options for the [DefaultSyncHandler].

<!-- references -->
[ExternalIdentityProvider]: /oak/docs/apidocs/org/apache/jackrabbit/oak/spi/security/authentication/external/ExternalIdentityProvider.html
[SyncHandler]: /oak/docs/apidocs/org/apache/jackrabbit/oak/spi/security/authentication/external/SyncHandler.html
[DefaultSyncHandler]: /oak/docs/apidocs/org/apache/jackrabbit/oak/spi/security/authentication/external/impl/DefaultSyncHandler.html
[org.apache.jackrabbit.oak.security.authentication.ldap.impl.LdapIdentityProvider]: /oak/docs/apidocs/org/apache/jackrabbit/oak/security/authentication/ldap/impl/LdapIdentityProvider.html
[org.apache.jackrabbit.oak.security.authentication.ldap.impl.LdapProviderConfig]: /oak/docs/apidocs/org/apache/jackrabbit/oak/security/authentication/ldap/impl/LdapProviderConfig.html
