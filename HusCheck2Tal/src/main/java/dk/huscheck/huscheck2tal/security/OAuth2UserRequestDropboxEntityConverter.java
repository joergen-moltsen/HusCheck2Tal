	/*
	 * Copyright 2002-2018 the original author or authors.
	 *
	 * Licensed under the Apache License, Version 2.0 (the "License");
	 * you may not use this file except in compliance with the License.
	 * You may obtain a copy of the License at
	 *
	 *      https://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS,
	 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 * See the License for the specific language governing permissions and
	 * limitations under the License.
	 */
package dk.huscheck.huscheck2tal.security;

import java.net.URI;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * A {@link Converter} that converts the provided {@link OAuth2UserRequest} to a {@link RequestEntity} representation of
 * a request for the UserInfo Endpoint.
 *
 * @author Joe Grandja
 * @see Converter
 * @see OAuth2UserRequest
 * @see RequestEntity
 * @since 5.1
 */
public class OAuth2UserRequestDropboxEntityConverter implements Converter<OAuth2UserRequest, RequestEntity<?>> {

    /**
     * Returns the {@link RequestEntity} used for the UserInfo Request.
     *
     * @param userRequest the user request
     * @return the {@link RequestEntity} used for the UserInfo Request
     */
	@Override
	public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
		ClientRegistration clientRegistration = userRequest.getClientRegistration();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		URI uri = UriComponentsBuilder.fromUriString(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
				.build()
				.toUri();
		headers.setBearerAuth(userRequest.getAccessToken().getTokenValue());

		return new RequestEntity<>("null", headers, HttpMethod.POST, uri);
	}	
}