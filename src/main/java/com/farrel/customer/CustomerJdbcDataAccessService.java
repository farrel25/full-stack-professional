package com.farrel.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository(value = "jdbc")
public class CustomerJdbcDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJdbcDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> findAllCustomers() {
        String sql = """
                SELECT id, name, email, age
                FROM customer
                """;

//        return jdbcTemplate.query(
//                sql,
//                (rs, rowNum) -> new Customer(
//                        rs.getLong("id"),
//                        rs.getString("name"),
//                        rs.getString("email"),
//                        rs.getInt("age")
//                )
//        );

//        return jdbcTemplate.query(
//                sql,
//                (rs, rowNum) -> customerRowMapper.mapRow(rs, rowNum)
//        );

//        return jdbcTemplate.query(sql, customerRowMapper::mapRow);

        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> findCustomerById(Long id) {
        String sql = """
                SELECT id, name, email, age
                FROM customer
                WHERE id = ?
                """;

//        return jdbcTemplate.query(sql, customerRowMapper, id)
//                .stream()
//                .findFirst();

        ResultSetExtractor<Customer> customerResultSetExtractor = rs -> rs.next() ?
                new Customer(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getInt("age")
                )
                :
                null;

        Customer customer = jdbcTemplate.query(
                sql,
                customerResultSetExtractor,
                id
        );

        return Optional.ofNullable(customer);
    }

    @Override
    public void insertCustomer(Customer customer) {
        String sql = """
                INSERT INTO customer (name, email, age)
                VALUES (?, ?, ?)
                """;

        int result = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );

        System.out.println("jdbcTemplate.update = " + result);
    }

    @Override
    public void updateCustomer(Customer customer) {
        String sql = """
                UPDATE customer
                SET name = ?, email = ?, age = ?
                WHERE id = ?
                """;

        int result = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                customer.getId()
        );

        System.out.println("jdbcTemplate.update = " + result);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = """
                SELECT EXISTS(
                    SELECT 1
                    FROM customer
                    WHERE id = ?
                )
                """;

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = """
                SELECT EXISTS(
                    SELECT 1
                    FROM customer
                    WHERE email = ?
                )
                """;

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, email));
    }

    @Override
    public void deleteCustomer(Long id) {
        String sql = """
                DELETE FROM customer
                WHERE id = ?
                """;

        int result = jdbcTemplate.update(sql, id);

        System.out.println("jdbcTemplate.update = " + result);
    }
}
