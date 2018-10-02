package com.forgerock.openbanking.exercise.tpp.api.account;

import com.forgerock.openbanking.exercise.tpp.model.aspsp.AspspConfiguration;
import com.forgerock.openbanking.exercise.tpp.repository.AspspConfigurationRepository;
import com.forgerock.openbanking.exercise.tpp.services.aspsp.rs.RSAccountAPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import uk.org.openbanking.datamodel.account.OBReadAccount2;

@Controller
public class AccountsAPIController implements AccountsAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountsAPIController.class);

    @Autowired
    private RSAccountAPIService rsAccountAPIService;
    @Autowired
    private AspspConfigurationRepository aspspConfigurationRepository;

    @Override
    public ResponseEntity readAccounts(
            @RequestParam(value = "aspspId") String aspspId,
            @RequestHeader(value = "accessToken") String accessToken) {
        //TODO: exercise: retrieve the aspsp configuration

        AspspConfiguration aspspConfiguration = aspspConfigurationRepository.findById(aspspId)
                .orElseThrow(IllegalArgumentException::new);


        //TODO exercise: call the RS-ASPSP accounts endpoint via the service
        try {
            OBReadAccount2 accounts = rsAccountAPIService.readAccounts(aspspConfiguration, accessToken);
            LOGGER.debug("Accounts response: {}",accounts );
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            LOGGER.error("Read accounts failed with error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
