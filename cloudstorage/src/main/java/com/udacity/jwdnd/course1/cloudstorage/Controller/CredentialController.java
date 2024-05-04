package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.Model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.Model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("credential")
public class CredentialController {
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService, EncryptionService encryptionService, UserService userService) {
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    @GetMapping
    public String getCredentialPage(Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
    @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential,
    Model model) {

        String userName = userService.getUserIdByAuthentication(authentication);

        model.addAttribute("credentials", credentialService.getCredentialListingsByName(userName));

        model.addAttribute("encryptionService", encryptionService);

        return "home";
        }


    @SuppressWarnings("unused")
    @PostMapping("insertCredential")
    public String addCredentials(
        Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
        @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential,
        Model model){
        Integer isAuthenticated = userService.getUserIdByAuthenticationSub(authentication);
        if (isAuthenticated == null) { 
            return "login";
        }
        credentialService.addOrUpdateCredential(newCredential, isAuthenticated);
        model.addAttribute("result", "success");
        return "result";
    }
}
