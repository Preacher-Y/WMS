package group.australia.warehouse_management_system.feature.item.exception;

import group.australia.warehouse_management_system.common.exceptions.NotFoundException;

public class ItemLocationNotFoundException extends NotFoundException {
    public ItemLocationNotFoundException(Long locationId) {
        super("Location with id " + locationId + " not found");
    }
}
