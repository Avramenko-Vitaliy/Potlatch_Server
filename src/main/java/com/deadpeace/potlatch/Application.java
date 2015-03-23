package com.deadpeace.potlatch;

import com.deadpeace.potlatch.client.PotlatchSvcApi;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

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
@EnableWebMvc
@EnableAutoConfiguration
public class Application extends WebMvcConfigurerAdapter
{
    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController(PotlatchSvcApi.SVC_LOGIN).setViewName("login");
        registry.addViewController(PotlatchSvcApi.GIFT_SVC_PATH).setViewName("gifts");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer)
    {
        configurer.ignoreAcceptHeader(true).defaultContentType(MediaType.TEXT_HTML);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer)
    {
        configurer.enable();
    }

   /* @Bean
    public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setContentNegotiationManager(manager);
        List<ViewResolver> resolvers = new ArrayList<ViewResolver>();
        resolvers.add(jsonViewResolver());
        resolvers.add(getViewResolver());
        resolver.setViewResolvers(resolvers);
        return resolver;
    }     */

    @Bean
    public InternalResourceViewResolver getViewResolver()
    {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

  /*  @Bean
    public ViewResolver jsonViewResolver()
    {
        return new JsonViewResolver();
    }*/

    @Bean
    public MultipartConfigElement multipartConfigElement()
    {
        MultipartConfigFactory factory = new MultipartConfigFactory();
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