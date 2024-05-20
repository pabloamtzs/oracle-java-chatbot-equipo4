package com.equipo4.chatbot.config;

import com.oracle.bmc.auth.InstancePrincipalsAuthenticationDetailsProvider;
import com.oracle.bmc.secrets.SecretsClient;
import com.oracle.bmc.secrets.model.Base64SecretBundleContentDetails;
import com.oracle.bmc.secrets.requests.GetSecretBundleRequest;
import com.oracle.bmc.secrets.responses.GetSecretBundleResponse;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("oci")
@Primary
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "spring.datasource")
public class DbConfig extends DataSourceProperties {
    
    private OciConfig ociConfig;

    public DbConfig(OciConfig ociConfig){
        this.ociConfig = ociConfig;
    }
    /*
    @Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		String username = getSecretFromVault(ociConfig.getRegion(), ociConfig.getVault().getDbUsernameOcid());
		this.setUsername(username);
		String password = getSecretFromVault(ociConfig.getRegion(), ociConfig.getVault().getDbPasswordOcid());
		this.setPassword(password);
        String url = getSecretFromVault(ociConfig.getRegion(), ociConfig.getVault().getDbUrlOcid());
        this.setUrl(url);
	}*/

    private String getSecretFromVault(String regionIdString, String secretOcid) {
        GetSecretBundleResponse getSecretBundleResponse;
        try (SecretsClient secretsClient = SecretsClient.builder()
                .build(InstancePrincipalsAuthenticationDetailsProvider.builder().build())) {
            secretsClient.setRegion(regionIdString);
            GetSecretBundleRequest getSecretBundleRequest = GetSecretBundleRequest
                    .builder()
                    .secretId(secretOcid)
                    .stage(GetSecretBundleRequest.Stage.Current)
                    .build();
            getSecretBundleResponse = secretsClient.getSecretBundle(getSecretBundleRequest);
        }
        Base64SecretBundleContentDetails base64SecretBundleContentDetails =
                (Base64SecretBundleContentDetails) getSecretBundleResponse.getSecretBundle().getSecretBundleContent();
        byte[] secretValueDecoded = java.util.Base64.getDecoder().decode(base64SecretBundleContentDetails.getContent());
        String secretValue = new String(secretValueDecoded);
        System.out.println("El valor del secreto ha sido recuperado correctamente con valor:" + secretValue);
        return secretValue;
    }
}
