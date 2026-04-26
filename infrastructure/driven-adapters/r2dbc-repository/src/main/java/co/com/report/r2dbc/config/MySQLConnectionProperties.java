package co.com.report.r2dbc.config;

public record MySQLConnectionProperties(
        String host,
        Integer port,
        String database,
        String username,
        String password) {
}