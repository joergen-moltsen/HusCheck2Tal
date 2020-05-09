package dk.huscheck.huscheck2tal.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

@RestController
public class UserController {

	@GetMapping("/user")
	public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
		return Collections.singletonMap("name", principal.getAttribute("email"));
	}

	@GetMapping("/avatar")
	public Map<String, Object> avatar(@AuthenticationPrincipal OAuth2User principal) {

		return Collections.singletonMap("avatar", principal.getAttribute("profile_photo_url"));
	}
	
	
	@GetMapping("/error")
	@ResponseBody
	public String error(HttpServletRequest request) {
		String message = (String) request.getSession().getAttribute("error.message");
		request.getSession().removeAttribute("error.message");
		return message;
	}
	
	@GetMapping("/case")
	public List<String> cloudFiles(
		@RegisteredOAuth2AuthorizedClient("dropbox-client") OAuth2AuthorizedClient authorizedClient) throws DbxException {
	    OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

		// Create Dropbox client
	    DbxRequestConfig config = new DbxRequestConfig("proof-of-concept-dropbox");
	    DbxClientV2 client = new DbxClientV2(config, accessToken.getTokenValue());

	    // Get files and folder metadata from Dropbox root directory
	    ListFolderResult result = client.files().listFolder("");
	    List<String> fileList = new ArrayList<>();
	    while (true) {
	      for (Metadata metadata : result.getEntries()) {
	        fileList.add(metadata.getPathDisplay());
	      }
	      if (!result.getHasMore()) {
	        break;
	      }
	      result = client.files().listFolderContinue(result.getCursor());
	    }
	    return fileList;
	}
	
//	TODO: @RestController
//	@PostMapping(path= "/case", consumes = "application/json", produces = "application/json")
//    public ResponseEntity<Object> addCase(@RequestBody Case case) {
//
//         
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//                                    .path("/{id}")
//                                    .buildAndExpand(case.getId())
//                                    .toUri();
//         
//        return ResponseEntity.created(location).build();
//    }
	
}
