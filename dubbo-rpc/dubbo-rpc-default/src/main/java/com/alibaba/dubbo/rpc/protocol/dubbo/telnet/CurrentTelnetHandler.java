/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.rpc.protocol.dubbo.telnet;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.telnet.TelnetHandler;
import com.alibaba.dubbo.remoting.telnet.support.Help;

/**
 * CurrentServiceTelnetHandler
 */
@Activate
@Help(parameter = "", summary = "Print working default service.", detail = "Print working default service.")
public class CurrentTelnetHandler implements TelnetHandler {

    public String telnet(Channel channel, String message) {
        if (message.length() > 0) {
            return "Unsupported parameter " + message + " for pwd.";
        }
        String service = (String) channel.getAttribute(ChangeTelnetHandler.SERVICE_KEY);
        StringBuilder buf = new StringBuilder();
        if (service == null || service.length() == 0) {
            buf.append("/");
        } else {
            buf.append(service);
        }
        return buf.toString();
    }

}