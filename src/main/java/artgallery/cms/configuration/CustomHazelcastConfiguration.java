package artgallery.cms.configuration;

import com.hazelcast.client.Client;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientConnectionStrategyConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

@Configuration
public class CustomHazelcastConfiguration {

  @Value("${app.hazelcast.addresses}")
  String[] hazelcastAddresses;

  @Value("${app.hazelcast.cluster-name}")
  String hazelcastClusterName;

  @Bean
  ClientConfig getHazelcastConfig() {
    ClientConfig clientConfig = new ClientConfig();
    clientConfig.getNetworkConfig().addAddress(hazelcastAddresses);
    clientConfig.setClusterName(hazelcastClusterName);
    clientConfig.getConnectionStrategyConfig()
      .setAsyncStart(true)
      .setReconnectMode(ClientConnectionStrategyConfig.ReconnectMode.ASYNC);
    return clientConfig;
  }
}
