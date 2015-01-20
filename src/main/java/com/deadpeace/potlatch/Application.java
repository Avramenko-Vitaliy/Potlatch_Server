package com.deadpeace.potlatch;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MultiPartConfigFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: DeadPeace
 * Date: 13.10.2014
 * Time: 11:35
 * To change this template use File | Settings | File Templates.
 */

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application
{
    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement()
    {
        MultiPartConfigFactory factory = new MultiPartConfigFactory();
        factory.setMaxFileSize("10MB");
        factory.setMaxRequestSize("10MB");
        return factory.createMultipartConfig();
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(@Value("${keystore.file:src/main/resources/private/keystore}") final String keystoreFile,@Value("${keystore.pass:Ns9sBV3Itx}") final String keystorePass) throws Exception
    {
        return new EmbeddedServletContainerCustomizer()
        {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container)
            {
                TomcatEmbeddedServletContainerFactory tomcat=(TomcatEmbeddedServletContainerFactory) container;
                tomcat.addConnectorCustomizers(new TomcatConnectorCustomizer()
                {
                    @Override
                    public void customize(Connector connector)
                    {
                        connector.setPort(8443);
                        connector.setSecure(true);
                        connector.setScheme("https");
                        Http11NioProtocol proto=(Http11NioProtocol) connector.getProtocolHandler();
                        proto.setSSLEnabled(true);
                        proto.setKeystoreFile(new File(keystoreFile).getAbsolutePath());
                        proto.setKeystorePass(keystorePass);
                        proto.setKeystoreType("JKS");
                        proto.setKeyAlias("tomcat");
                    }
                });
            }
        };
    }
}