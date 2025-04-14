package com.social.socialnetwork.Repository;

import com.social.socialnetwork.Domain.CererePrietenie;
import com.social.socialnetwork.Domain.Prietenie;
import com.social.socialnetwork.Domain.Tuplu;
import com.social.socialnetwork.Validators.FactoryValidator;
import com.social.socialnetwork.Validators.Validator;
import com.social.socialnetwork.Validators.ValidatorStrategies;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

public class CereriPrieteniiDBRepository implements Repository<Tuplu<Long, Long>, CererePrietenie>{
    private Validator cererePrietenieValidator;
    private DBConnection dbConnection;

    public CereriPrieteniiDBRepository(DBConnection dbConnection, ValidatorStrategies validatorStrategies) {
        cererePrietenieValidator = FactoryValidator.getFactoryInstance().createValidator(validatorStrategies);
        this.dbConnection = dbConnection;
    }

    @Override
    public Optional<CererePrietenie> findOne(Tuplu<Long, Long> longLongTuplu) {
        try(Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM CereriPrietenie " +
                    "WHERE (iduser1 = ? AND iduser2 = ?) OR (iduser1 = ? AND iduser2 = ?)");

        ) {
            statement.setLong(1, longLongTuplu.getLeft());
            statement.setLong(2, longLongTuplu.getRight());
            statement.setLong(3, longLongTuplu.getRight());
            statement.setLong(4, longLongTuplu.getLeft());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                Long idSender = resultSet.getLong("iduser1");
                Long idRecv = resultSet.getLong("iduser2");
                Integer status = resultSet.getInt("stare_cerere");
                LocalDateTime friendsFrom = null;
                var data = resultSet.getTimestamp("datecreated");
                if(data!=null){
                    friendsFrom = data.toLocalDateTime();
                }
                CererePrietenie prietenie = new CererePrietenie(idSender, idRecv, friendsFrom, status);
                return Optional.of(prietenie);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }


    @Override
    public Iterable<CererePrietenie> findAll() {
        HashMap<Tuplu<Long, Long>, CererePrietenie> entities = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM cereriprietenie");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long idSender = resultSet.getLong("iduser1");
                Long idRecv = resultSet.getLong("iduser2");
                Integer status = resultSet.getInt("stare_cerere");
                LocalDateTime friendsFrom = null;
                var data = resultSet.getTimestamp("datecreated");
                if(data!=null){
                    friendsFrom = data.toLocalDateTime();
                }
                CererePrietenie cererePrietenie = new CererePrietenie(idSender, idRecv, friendsFrom, status);
                entities.put(cererePrietenie.getId(), cererePrietenie);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entities.values();
    }

    public Iterable<CererePrietenie> findCereriUser(Long idUser){
        HashMap<Tuplu<Long, Long>, CererePrietenie> entities = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM cereriprietenie WHERE iduser1 = ? OR iduser2 = ?");
            statement.setLong(1, idUser);
            statement.setLong(2, idUser);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long idSender = resultSet.getLong("iduser1");
                Long idRecv = resultSet.getLong("iduser2");
                Integer status = resultSet.getInt("stare_cerere");
                LocalDateTime friendsFrom = null;
                var data = resultSet.getTimestamp("datecreated");
                if(data!=null){
                    friendsFrom = data.toLocalDateTime();
                }
                CererePrietenie cererePrietenie = new CererePrietenie(idSender, idRecv, friendsFrom, status);
                entities.put(cererePrietenie.getId(), cererePrietenie);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entities.values();
    }

    @Override
    public Optional<CererePrietenie> save(CererePrietenie entity) {
        cererePrietenieValidator.validate(entity);
        if(findOne(entity.getId()).isPresent()){
            return Optional.of(entity);
        }
        String sqlStatement = "INSERT INTO cereriprietenie(iduser1, iduser2, datecreated, stare_cerere) VALUES (?, ?, ?, ?);";
        var optional = findOne(entity.getId());
        if(optional.isPresent()){
            return Optional.of(entity);
        }
        try(Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)){
            preparedStatement.setLong(1, entity.getId().getLeft());
            preparedStatement.setLong(2, entity.getId().getRight());
            if(entity.getDateCreated()==null){
                preparedStatement.setTimestamp(3, null);
            }
            else{
                preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getDateCreated()));
            }
            preparedStatement.setInt(4, entity.getStatus());
            var responseSQL =preparedStatement.executeUpdate();
            return responseSQL==0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CererePrietenie> delete(Tuplu<Long, Long> longLongTuplu) {
        String sqlStatement = "DELETE FROM prietenii P WHERE (P.iduser1 = ? AND P.iduser2 = ?) OR (P.iduser2 = ? AND P.iduser1 = ?)";
        try(Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)){
            preparedStatement.setLong(1, longLongTuplu.getLeft());
            preparedStatement.setLong(2, longLongTuplu.getRight());
            preparedStatement.setLong(3, longLongTuplu.getLeft());
            preparedStatement.setLong(4, longLongTuplu.getRight());
            var entitatea = findOne(longLongTuplu);
            var raspuns = 0;
            if(entitatea.isPresent()){
                raspuns = preparedStatement.executeUpdate();
            }
            return raspuns==0 ? Optional.empty() : entitatea;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<CererePrietenie> update(CererePrietenie entity) {
        String sqlStatement = "UPDATE cereriprietenie P SET datecreated = ?, stare_cerere = ? WHERE (P.iduser1 = ? AND P.iduser2 = ?) OR (P.iduser2 = ? AND P.iduser1 = ?)";
        cererePrietenieValidator.validate(entity);
        try(Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        ){
            if(entity.getDateCreated()==null){
                preparedStatement.setTimestamp(1, null);
            }
            else{
                preparedStatement.setTimestamp(1, Timestamp.valueOf(entity.getDateCreated()));
            }
            preparedStatement.setInt(2, entity.getStatus());
            preparedStatement.setLong(3 ,entity.getId().getLeft());
            preparedStatement.setLong(4 ,entity.getId().getRight());
            preparedStatement.setLong(5 ,entity.getId().getLeft());
            preparedStatement.setLong(6 ,entity.getId().getRight());
            var response = preparedStatement.executeUpdate();
            return response==0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        try (Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD)) {
            PreparedStatement statement = connection.prepareStatement("SELECT count(*) FROM cereriprietenie");
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
