package DAL.repository.jpa;

import DAL.entity.Order;
import DAL.model.QueryOrdersDalModel;
import DAL.repository.jdbc.IOrderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;


@Repository
public interface IOrderRepository extends JpaRepository<Order, Long>, IOrderRepositoryCustom {}
