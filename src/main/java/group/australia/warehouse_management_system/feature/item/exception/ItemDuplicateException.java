package group.australia.warehouse_management_system.feature.item.exception;

import group.australia.warehouse_management_system.common.exceptions.ConflictException;

public class ItemDuplicateException extends ConflictException {
    public ItemDuplicateException(String itemNumber, Long locationId) {
        super("Item " + itemNumber + " at location " + locationId + " already exists");
    }
}
