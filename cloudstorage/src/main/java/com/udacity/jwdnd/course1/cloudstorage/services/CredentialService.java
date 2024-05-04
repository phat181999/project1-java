package com.udacity.jwdnd.course1.cloudstorage.services;


import org.springframework.stereotype.Service;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.Mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.Model.Credentials;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final UserMapper userMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, UserMapper userMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }

    public void addOrUpdateCredential(CredentialForm credentialForm, Integer userId) {
        byte[] key = generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentialForm.getPassword(), encodedKey);
        
        Credentials existingCredential = credentialMapper.getCredentialByUserName(credentialForm.getUserName());
        
        if (existingCredential == null) {
            Credentials newCredential = new Credentials(
                    null,
                    credentialForm.getUrl(),
                    credentialForm.getUserName(),
                    encryptedPassword,
                    userId,
                    encodedKey
            );
            credentialMapper.insert(newCredential);
        } else {
            credentialMapper.updateCredential(credentialForm.getUserName(), credentialForm.getUrl(), encodedKey, encryptedPassword);
        }
    }

    public List<Credentials> getListCredential(Integer userId) {
        return credentialMapper.getCredentialListings(userId);
    }

    public List<Credentials> getAllCredential() {
        return credentialMapper.getAllCredential();
    }

    public List<Credentials> getCredentialListingsByName(String userName) {
        return credentialMapper.getCredentialListingsByName(userName);
    }

    public void deleteCredential(String userName) {
        credentialMapper.deleteCredential(userName);
    }


    public boolean checkCredentialExist(String userName) {
        return credentialMapper.getCredentialByUserName(userName) != null;
    }

    private byte[] generateKey() {
        byte[] key = new byte[16];
        new SecureRandom().nextBytes(key);
        return key;
    }
}
