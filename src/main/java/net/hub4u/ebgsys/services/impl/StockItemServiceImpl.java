package net.hub4u.ebgsys.services.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.hub4u.ebgsys.entities.StockItem;
import net.hub4u.ebgsys.repositories.StockItemRepository;
import net.hub4u.ebgsys.services.StockItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockItemServiceImpl implements StockItemService {

    @Autowired
    StockItemRepository stockItemRepository;

    @Override
    public StockItem createStockItem(StockItem stockItem) {
        return stockItemRepository.save(stockItem);
    }

    @Override
    public List<StockItem> fetchAllStockItems() {
        Iterable<StockItem>  stockItemIterable = stockItemRepository.findAll();
        List<StockItem> stockItems   = new ArrayList<>();
        stockItemIterable.forEach(stockItems::add);
        return stockItems;
    }

    @Override
    public Optional<StockItem> fetchByProductReference(String productReference) {
        return stockItemRepository.findByProductReference(productReference);
    }

    @Override
    public StockItem updateStockItem(StockItem stockItem) {

        StockItem currentStockItem = fetchByProductReference(stockItem.getProductReference()).orElseThrow(
                () -> new IllegalArgumentException("fetchSale() - Failed finding StockItem with productReference:" + stockItem.getProductReference()) );

        currentStockItem.setStatus(stockItem.getStatus());
        currentStockItem.setQuantity(stockItem.getQuantity());
        currentStockItem.setInDate(stockItem.getInDate());
        currentStockItem.setOutDate(stockItem.getOutDate());

        currentStockItem.setInQuantity(stockItem.getInQuantity());
        currentStockItem.setOutQuantity(stockItem.getOutQuantity());

        return stockItemRepository.save(currentStockItem);
    }
}
