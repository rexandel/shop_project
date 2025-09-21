package DAL.repository.jdbc;

import DAL.entity.OrderItem;
import DAL.jdbc.UnitOfWork;
import DAL.model.QueryOrderItemsDalModel;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderItemRepositoryImpl implements IOrderItemRepositoryCustom {
    private final UnitOfWork unitOfWork;

    public OrderItemRepositoryImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    public List<OrderItem> bulkInsert(List<OrderItem> items) throws SQLException {
        unitOfWork.bulkInsertOrderItems(items);
        return items;
    }
}
