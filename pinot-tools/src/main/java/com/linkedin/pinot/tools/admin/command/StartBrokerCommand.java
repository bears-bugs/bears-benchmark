/**
 * Copyright (C) 2014-2016 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkedin.pinot.tools.admin.command;

import com.linkedin.pinot.tools.Command;
import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkedin.pinot.broker.broker.helix.HelixBrokerStarter;
import com.linkedin.pinot.common.utils.CommonConstants;
import com.linkedin.pinot.common.utils.NetUtil;


/**
 * Class to implement StartBroker command.
 *
 */
public class StartBrokerCommand extends AbstractBaseAdminCommand implements Command {
  private static final Logger LOGGER = LoggerFactory.getLogger(StartBrokerCommand.class);

  @Option(name = "-brokerHost", required = false, metaVar = "<String>", usage = "host name for controller.")
  private String _brokerHost;

  @Option(name = "-brokerPort", required = false, metaVar = "<int>", usage = "Broker port number to use for query.")
  private int _brokerPort = CommonConstants.Helix.DEFAULT_BROKER_QUERY_PORT;;

  @Option(name = "-zkAddress", required = false, metaVar = "<http>", usage = "HTTP address of Zookeeper.")
  private String _zkAddress = DEFAULT_ZK_ADDRESS;

  @Option(name = "-clusterName", required = false, metaVar = "<String>", usage = "Pinot cluster name.")
  private String _clusterName = "PinotCluster";

  @Option(name = "-configFileName", required = false, metaVar = "<Config File Name>",
      usage = "Broker Starter Config file.", forbids = { "-brokerHost", "-brokerPort" })
  private String _configFileName;

  @Option(name = "-help", required = false, help = true, aliases = { "-h", "--h", "--help" },
      usage = "Print this message.")
  private boolean _help = false;

  public boolean getHelp() {
    return _help;
  }

  @Override
  public String getName() {
    return "StartBroker";
  }

  @Override
  public String toString() {
    if (_configFileName != null) {
      return ("StartBroker -zkAddress " + _zkAddress + " -configFileName " + _configFileName);
    } else {
      return ("StartBroker -brokerHost " + _brokerHost + " -brokerPort " + _brokerPort + " -zkAddress " + _zkAddress);
    }
  }

  @Override
  public void cleanup() {

  }

  @Override
  public String description() {
    return "Start the Pinot Broker process at the specified port";
  }

  public StartBrokerCommand setClusterName(String clusterName) {
    _clusterName = clusterName;
    return this;
  }

  public StartBrokerCommand setPort(int port) {
    _brokerPort = port;
    return this;
  }

  public StartBrokerCommand setZkAddress(String zkAddress) {
    _zkAddress = zkAddress;
    return this;
  }

  public StartBrokerCommand setConfigFileName(String configFileName) {
    _configFileName = configFileName;
    return this;
  }

  @Override
  public boolean execute() throws Exception {
    try {
      Configuration configuration = readConfigFromFile(_configFileName);
      if (configuration == null) {
        if (_configFileName != null) {
          LOGGER.error("Error: Unable to find file {}.", _configFileName);
          return false;
        }

        configuration = new PropertiesConfiguration();
        configuration.addProperty(CommonConstants.Helix.KEY_OF_BROKER_QUERY_PORT, _brokerPort);
        configuration.setProperty("pinot.broker.routing.table.builder.class", "random");
      }

      LOGGER.info("Executing command: " + toString());
      final HelixBrokerStarter pinotHelixBrokerStarter =
          new HelixBrokerStarter(_brokerHost, _clusterName, _zkAddress, configuration);

      String pidFile = ".pinotAdminBroker-" + String.valueOf(System.currentTimeMillis()) + ".pid";
      savePID(System.getProperty("java.io.tmpdir") + File.separator + pidFile);
      return true;
    } catch (Exception e) {
      LOGGER.error("Caught exception while starting broker, exiting", e);
      System.exit(-1);
      return false;
    }
  }
}
