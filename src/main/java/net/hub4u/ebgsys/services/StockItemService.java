package net.hub4u.ebgsys.services;

import net.hub4u.ebgsys.entities.StockItem;

import java.util.List;
import java.util.Optional;

public interface StockItemService {

    StockItem createStockItem(StockItem stockItem);

    List<StockItem> fetchAllStockItems();

    Optional<StockItem> fetchByProductReference(String productReference);

    StockItem updateStockItem(StockItem stockItem);
}
