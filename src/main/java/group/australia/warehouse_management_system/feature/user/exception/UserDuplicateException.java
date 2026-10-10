package group.australia.warehouse_management_system.feature.user.exception;

import group.australia.warehouse_management_system.common.exceptions.ConflictException;

public class UserDuplicateException extends ConflictException {
    public UserDuplicateException(String email) {
        super("User with email \""+email+"\" already exists");
    }
}
