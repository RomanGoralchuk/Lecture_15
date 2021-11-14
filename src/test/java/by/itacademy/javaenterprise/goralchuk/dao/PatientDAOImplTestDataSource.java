package by.itacademy.javaenterprise.goralchuk.dao;

import by.itacademy.javaenterprise.goralchuk.entity.Patient;
import by.itacademy.javaenterprise.goralchuk.entity.PatientSex;
import com.github.dockerjava.api.model.Device;
import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.junit.*;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


public class PatientDAOImplTestDataSource {
    private static DataSource dataSource;
    private static Connection connection;
    private static PreparedStatement statement;
    private static ResultSet resultSet;
    private static JdbcTemplate jdbcTemplate;
    private static NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static PatientDAOImpl patientDAO;
    private static final Logger logger = LoggerFactory.getLogger(PatientDAOImplTestDataSource.class);

    @Rule
    public TestWatcher watchman= new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            logger.error("Test failed: " + description);
        }
        @Override
        protected void succeeded(Description description) {
            logger.info("Test successes: " + description);
        }
    };

    @BeforeClass
    public static void beforeClass() throws Exception {
        dataSource = mock(DataSource.class);
        patientDAO = mock(PatientDAOImpl.class);
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @AfterClass
    public static void afterClass() throws Exception {

    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void whenLookingForAPatientById() {
        Long actualId = 10L;
        Long expectedId = 10L;
        Patient patientTest = new Patient();
        patientTest.setId(actualId);
        when(namedParameterJdbcTemplate.queryForObject(anyString(), Mockito.<Map<String, ?>>any(),
                ArgumentMatchers.<RowMapper<Patient>>any())).thenReturn(patientTest);
        assertEquals(expectedId, patientTest.getId());
    }

    @Test
    public void whenWeSaveAPatientToTheDataBase() throws SQLException {
        Patient patientTest = new Patient(
                "TestName",
                "TestSurname",
                PatientSex.M,
                java.sql.Date.valueOf("1111-11-11"));
        when(jdbcTemplate.update(Mockito.anyString(), Mockito.<Map<String, ?>>any())).thenReturn(1);
        assertEquals(1, patientDAO.save(patientTest));
    }

    @Test
    public void whenWeGetAListOfAllPersons() {
        when(namedParameterJdbcTemplate.query(Mockito.anyString(), Mockito.<RowMapper<Patient>>any()))
                .thenThrow(RecoverableDataAccessException.class);
        assertTrue(patientDAO.findAllPersons().isEmpty());
    }

    @Test
    public void whenWeFindPatientsBySex() {
        when(namedParameterJdbcTemplate.query(Mockito.anyString(), Mockito.<RowMapper<Patient>>any()))
                .thenThrow(RecoverableDataAccessException.class);
        assertTrue(patientDAO.findBySexPatients(PatientSex.M).isEmpty());
    }
}