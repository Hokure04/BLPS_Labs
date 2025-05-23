package blps.labs.service;

import org.keycloak.credential.CredentialModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionTask;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.KeycloakModelUtils;

public class RegisterServiceImpl implements RegisterService {

    private final KeycloakSession session;

    public RegisterServiceImpl(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Boolean register(User user) {
        try {
            KeycloakModelUtils.runJobInTransaction(session.getKeycloakSessionFactory(), session -> {
                RealmModel realm = session.getContext().getRealm();
                UserModel userModel = session.users().addUser(realm, user.getUsername());
                userModel.setEnabled(true);
                userModel.setEmail(user.getEmail());
                // Set password
//                CredentialModel credential = CredentialModel.password(user.getPassword());
//                session.userCredentialManager().updateCredential(realm, userModel, credential);
//                return null;
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
