package com.jgoodies.sample;

import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.ValidationUtils;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 29.09.2010 23:31:38 (Europe/Moscow)
 */
public class ValidatorTest {

    public interface User {
        public String getUserName();

        public void setUserName(final String pUserName);

        public String getPassword();

        public void setPassword(final String pPassword);
    }

    public class UserImpl implements User {
        private String userName;
        private String password;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public class UserValidator extends AbstractValueModel implements Validator<User>, User {
        private User mUser = new UserImpl();

        public Object getValue() {
            return this;
        }

        public void setValue(Object pObject) {
            // never used
        }

        public User getUser() {
            return mUser;
        }

        public void setUser(final User pUser) {
            mUser = pUser;
        }

        public ValidationResult validate(User validationTarget) {
            ValidationResult result = new ValidationResult();

            if (ValidationUtils.isEmpty(mUser.getUserName())) {
                result.addError("User name is required");
            }
            if (ValidationUtils.isEmpty(mUser.getPassword())) {
                result.addError("Password is required");
            }
            return result;
        }

        public String getUserName() {
            return mUser.getUserName();
        }

        public void setUserName(final String pUserName) {
            mUser.setUserName(pUserName);
        }

        public String getPassword() {
            return mUser.getPassword();
        }

        public void setPassword(final String pPassword) {
            mUser.setPassword(pPassword);
        }
    }

    public static void main(String[] args) {

    }

}
