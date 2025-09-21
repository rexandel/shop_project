package DAL.repository.jdbc;

import DAL.entity.Order;
import DAL.model.QueryOrdersDalModel;

import java.sql.SQLException;
import java.util.List;

public interface IOrderRepositoryCustom {
    List<Order> bulkInsert(List<Order> orders) throws SQLException;
}