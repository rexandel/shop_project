package DAL.repository.jdbc;

import DAL.entity.Order;
import DAL.jdbc.UnitOfWork;
import DAL.model.QueryOrdersDalModel;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements IOrderRepositoryCustom {
    private final UnitOfWork unitOfWork;

    public OrderRepositoryImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    public List<Order> bulkInsert(List<Order> orders) throws SQLException {
        unitOfWork.bulkInsertOrders(orders);
        return orders;
    }
}

