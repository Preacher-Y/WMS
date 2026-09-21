package group.australia.warehouse_management_system.feature.item.exception;

import group.australia.warehouse_management_system.common.exceptions.NotFoundException;

public class ItemCartonHeaderNotFoundException extends NotFoundException {
    public ItemCartonHeaderNotFoundException(Long cartonHeaderId) {
        super("Carton Header with id " + cartonHeaderId + " not found");
    }
}
