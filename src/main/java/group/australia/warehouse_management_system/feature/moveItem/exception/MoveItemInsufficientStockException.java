package group.australia.warehouse_management_system.feature.moveItem.exception;

import group.australia.warehouse_management_system.common.exceptions.BadRequestException;

public class MoveItemInsufficientStockException extends BadRequestException {

    public MoveItemInsufficientStockException(String itemNumber, Integer available, Integer requested) {
        super("Item " + itemNumber + " insufficient stock: available " + available + ", requested " + requested);
    }
}
