package com.farrel.customer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();

        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("name")).thenReturn("Farrel");
        Mockito.when(resultSet.getString("email")).thenReturn("farrel@domain.com");
        Mockito.when(resultSet.getInt("age")).thenReturn(17);

        // When
        Customer actual = customerRowMapper.mapRow(resultSet, 1);

        // Then
        Customer expected = new Customer(1L, "Farrel", "farrel@domain.com", 17);

        Assertions.assertThat(actual).isEqualTo(expected);
    }
}