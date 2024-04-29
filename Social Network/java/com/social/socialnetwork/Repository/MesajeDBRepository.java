package com.social.socialnetwork.Repository;

import com.social.socialnetwork.Domain.Mesaj;
import com.social.socialnetwork.Domain.Utilizator;
import com.social.socialnetwork.Validators.FactoryValidator;
import com.social.socialnetwork.Validators.Validator;
import com.social.socialnetwork.Validators.ValidatorStrategies;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class MesajeDBRepository implements Repository<Long, Mesaj>{
    private DBConnection dbConnection;

    public MesajeDBRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public Iterable<Mesaj> findMesajeBetween(Long idUser1, Long idUser2){
        HashMap<Long, Mesaj> mesaje = new HashMap<>();
        String sqlStatement = "SELECT M.idmesaj FROM Mesaje M INNER JOIN Destinatari D on M.idmesaj = D.idmesaj " +
                                "WHERE (M.idSender = ? AND D.idUser = ?) OR (M.idSender = ? AND D.idUser = ?);";
        try(Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
        ) {
            statement.setLong(1, idUser1);
            statement.setLong(2, idUser2);
            statement.setLong(3, idUser2);
            statement.setLong(4, idUser1);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long idMesaj = resultSet.getLong("idmesaj");
                var mesajLoaded = this.findOne(idMesaj).get();
                mesaje.put(idMesaj, mesajLoaded);
            }
            return mesaje.values();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<Mesaj> findOne(Long aLong) {
        String sqlStatement = "SELECT * FROM mesaje M WHERE M.idmesaj = ? ";
        try(Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
        ) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                Mesaj mesajCreat = null;
                Long idMesaj = resultSet.getLong("idmesaj");
                Long idSender = resultSet.getLong("idSender");
                List<Long> idDestinatari = new ArrayList<>();
                String mesajText = resultSet.getString("mesaj");
                LocalDateTime data = resultSet.getTimestamp("dateSend").toLocalDateTime();
                String altSqlStatement = "SELECT * FROM destinatari WHERE idmesaj = ?";
                try (Connection connection1 = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
                     PreparedStatement altStatement = connection1.prepareStatement(altSqlStatement);
                ) {
                    altStatement.setLong(1, aLong);
                    ResultSet resultSet2 = altStatement.executeQuery();
                    while (resultSet2.next()){
                        Long idDestinatar = resultSet2.getLong("iduser");
                        idDestinatari.add(idDestinatar);
                    }
                    mesajCreat = new Mesaj(idSender, idDestinatari,mesajText, data);
                    mesajCreat.setId(idMesaj);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                String alt2SqlStatement = "SELECT idmesajinitial FROM replymesaje WHERE idmesajreply=?";
                try (Connection connection2 = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
                     PreparedStatement altStatement = connection2.prepareStatement(alt2SqlStatement);
                ) {
                    altStatement.setLong(1, mesajCreat.getId());
                    ResultSet resultSet3 = altStatement.executeQuery();
                    if (resultSet3.next()){
                        Long idMesajInitial = resultSet3.getLong("idmesajinitial");
                        var mesajInitial = this.findOne(idMesajInitial).get();
                        mesajCreat.setReplyTo(mesajInitial);
                    }
                    else{
                        mesajCreat.setReplyTo(null);
                    }
                    return Optional.of(mesajCreat);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Mesaj> findAll() {
        HashMap<Long, Mesaj> entities = new HashMap<>();
        String sqlStatement = "SELECT * FROM mesaje";
        try (Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD)) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long idMesaj = resultSet.getLong("idmesaj");
                Long idSender = resultSet.getLong("idSender");
                List<Long> idDestinatari = new ArrayList<>();
                String mesajText = resultSet.getString("mesaj");
                LocalDateTime data = resultSet.getTimestamp("dateSend").toLocalDateTime();
                String altSqlStatement = "SELECT * FROM destinatari WHERE idmesaj = ?";
                try (Connection connection1 = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
                     PreparedStatement altStatement = connection1.prepareStatement(altSqlStatement);
                ) {
                    altStatement.setLong(1, idMesaj);
                    ResultSet resultSet2 = altStatement.executeQuery();
                    while (resultSet2.next()) {
                        Long idDestinatar = resultSet2.getLong("iduser");
                        idDestinatari.add(idDestinatar);
                    }
                    Mesaj mesajCreat = new Mesaj(idSender, idDestinatari, mesajText, data);
                    mesajCreat.setId(idMesaj);
                    entities.put(idMesaj, mesajCreat);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entities.values();
    }

    @Override
    public Optional<Mesaj> save(Mesaj entity) {
        try(Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO mesaje(idsender, mesaj, datesend) VALUES(?, ?, ?) RETURNING idmesaj;")){
            preparedStatement.setLong(1, entity.getFromUser());
            preparedStatement.setString(2, entity.getMesajScris());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getData()));
            var responseSQL = preparedStatement.executeQuery();
            if(!responseSQL.next()) {
                return Optional.of(entity);
            }
            Long idMesajSalvat = responseSQL.getLong("idmesaj");
            entity.setId(idMesajSalvat);
            entity.getToUsers().forEach(x->{
                String altSqlStatement = "INSERT INTO destinatari(idmesaj, iduser) VALUES (?, ?)";
                try (Connection connection1 = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
                     PreparedStatement altStatement = connection1.prepareStatement(altSqlStatement);
                ) {
                    altStatement.setLong(1, idMesajSalvat);
                    altStatement.setLong(2, x);
                    var resultSet2 = altStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            if(entity.getReplyTo() == null){
                return Optional.empty();
            }
            String altSqlStatement = "INSERT INTO replymesaje(IDMESAJINITIAL, IDMESAJREPLY) VALUES (?, ?)";
            try (Connection connection1 = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
                 PreparedStatement altStatement = connection1.prepareStatement(altSqlStatement);
            ) {
                altStatement.setLong(1, entity.getReplyTo().getId());
                altStatement.setLong(2, entity.getId());
                var resultSet2 = altStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Mesaj> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Mesaj> update(Mesaj entity) {
        return Optional.empty();
    }

    @Override
    public int size() {
        try(Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM mesaje")
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
