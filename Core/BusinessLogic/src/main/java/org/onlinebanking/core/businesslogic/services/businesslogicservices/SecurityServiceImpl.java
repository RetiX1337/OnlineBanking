package org.onlinebanking.core.businesslogic.services.businesslogicservices;

import com.google.common.hash.Hashing;
import org.onlinebanking.core.businesslogic.services.SecurityService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class SecurityServiceImpl implements SecurityService {
    @Override
    public String hash(String stringToHash) {
        return Hashing.sha256()
                .hashString(stringToHash, StandardCharsets.UTF_8)
                .toString();
    }
}
