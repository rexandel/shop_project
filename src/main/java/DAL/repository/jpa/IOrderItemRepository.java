package DAL.repository.jpa;

import DAL.entity.OrderItem;
import DAL.model.QueryOrderItemsDalModel;
import DAL.repository.jdbc.IOrderItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItem, Long>, IOrderItemRepositoryCustom {}
