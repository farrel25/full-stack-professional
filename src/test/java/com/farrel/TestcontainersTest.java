package com.farrel;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

//@SpringBootTest
// You should never use @SpringBootTest for unit test, because this will spin up the entire application if not configure properly,
// and also load the application context that has bunch of beans that we might not even care about when we are within tests.
// But for integration test, feel free to use it.
public class TestcontainersTest extends AbstractTestcontainers {

    @Test
    void canStartPostgresDB() {
        // export FORMAT="ID\t{{.ID}}\nNAME\t{{.Names}}\nIMAGE\t{{.Image}}\nPORTS\t{{.Ports}}\nCOMMAND\t{{.Command}}\nCREATED\t{{.CreatedAt}}\nSTATUS\t{{.Status}}\n"
        // docker ps --format="$FORMAT"
        // psql -U farrel -d farrel-dao-unit-test
        Assertions.assertThat(postgreSQLContainer.isRunning()).isTrue();
        Assertions.assertThat(postgreSQLContainer.isCreated()).isTrue();
    }
}
