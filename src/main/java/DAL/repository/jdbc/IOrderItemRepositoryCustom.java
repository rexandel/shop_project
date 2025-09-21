package DAL.repository.jdbc;

import DAL.entity.OrderItem;
import DAL.model.QueryOrderItemsDalModel;

import java.sql.SQLException;
import java.util.List;


public interface IOrderItemRepositoryCustom {
    List<OrderItem> bulkInsert(List<OrderItem> items) throws SQLException;
}
