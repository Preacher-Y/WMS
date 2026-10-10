package group.australia.warehouse_management_system.feature.user.exception;

import group.australia.warehouse_management_system.common.exceptions.NotFoundException;

public class UserNotFound extends NotFoundException {

    public UserNotFound(Long id) {
        super("User with id \""+id+"\" not found");
    }
}
