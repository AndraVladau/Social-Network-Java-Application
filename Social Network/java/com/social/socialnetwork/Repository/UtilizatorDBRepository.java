package com.social.socialnetwork.Repository;

import com.social.socialnetwork.Domain.Utilizator;
import com.social.socialnetwork.Repository.Paging.IPagingRepository;
import com.social.socialnetwork.Repository.Paging.Page;
import com.social.socialnetwork.Repository.Paging.Pageable;
import com.social.socialnetwork.Validators.FactoryValidator;
import com.social.socialnetwork.Validators.Validator;
import com.social.socialnetwork.Validators.ValidatorStrategies;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


public class UtilizatorDBRepository implements IPagingRepository<Long, Utilizator> {
    private Validator utilizatorValidator;
    private DBConnection dbConnection;

    public UtilizatorDBRepository(DBConnection dbConnection, ValidatorStrategies validatorStrategies) {
        utilizatorValidator = FactoryValidator.getFactoryInstance().createValidator(validatorStrategies);
        this.dbConnection = dbConnection;
    }


    @Override
    public Optional<Utilizator> findOne(Long longID) {
        try(Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
            PreparedStatement statement = connection.prepareStatement("select * from Utilizatori " +
                    "where idUser = ?");

        ) {
            statement.setLong(1, longID);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                Utilizator u = new Utilizator(firstName,lastName);
                u.setId(longID);
                u = loadFriends(u).get();
                return Optional.ofNullable(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }


    @Override
    public Iterable<Utilizator> findAll() {
        HashMap<Long, Utilizator> entities = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Utilizatori");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("iduser");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                Utilizator utilizator = new Utilizator(firstName, lastName);
                utilizator.setId(id);
                utilizator = loadFriends(utilizator).get();
                entities.put(id, utilizator);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entities.values();
    }

    /*
    -modificare entity
    -modificare save
    -modificare GUI

    * */


    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        utilizatorValidator.validate(entity);
        try(Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Utilizatori(firstname, lastname,username,password) VALUES(?, ?,?,?);")){
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3,entity.getUsername());
            preparedStatement.setString(4,entity.getHashedPassword());
            var responseSQL =preparedStatement.executeUpdate();
            return responseSQL==0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Utilizator> delete(Long aLong) {
        try(Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Utilizatori U WHERE U.iduser = ?")){
            preparedStatement.setLong(1, aLong);
            var entitatea = findOne(aLong);
            var raspuns = 0;
            if(entitatea.isPresent()){
                raspuns = preparedStatement.executeUpdate();
            }
            return raspuns==0 ? Optional.empty() : entitatea;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<Utilizator> loadFriends(Utilizator utilizator){
        String sqlStatement = "SELECT U.* FROM Utilizatori U, Prietenii P\n" +
                "WHERE (P.iduser1 = ? AND P.iduser2 = u.IDUser) OR (P.iduser2 = ? AND P.iduser1 = u.IDUser);";
        try(Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)
        ) {
            preparedStatement.setLong(1, utilizator.getId());
            preparedStatement.setLong(2, utilizator.getId());
            var resultSet =preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("iduser");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                Utilizator utilizatorFriend = new Utilizator(firstName, lastName);
                utilizatorFriend.setId(id);
                utilizator.addFriend(utilizatorFriend);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(utilizator);
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        utilizatorValidator.validate(entity);
        try(Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Utilizatori SET firstname = ?, lastname=? WHERE idUser = ?")
        ){
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setLong(3 ,entity.getId());
            var response = preparedStatement.executeUpdate();
            return response==0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        try(Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER,
                dbConnection.DB_PASSWD);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM utilizatori")
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Utilizator> findAll(Pageable pageable)
    {
        int numberOfElements = size();
        int limit = pageable.getPageSize();
        int offset = pageable.getPageSize()*pageable.getPageNumber();
        System.out.println(offset + " ?>= "+numberOfElements);
        if(offset >= numberOfElements)
            return new Page<>(new ArrayList<>(), numberOfElements);


        List<Utilizator> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(dbConnection.DB_URL,
                dbConnection.DB_USER, dbConnection.DB_PASSWD);
            PreparedStatement statement = connection.prepareStatement
                    ("select * from utilizatori offset ? limit ?");
        )
        {
            statement.setInt(1, offset);
            statement.setInt(2,limit);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Long id = resultSet.getLong("iduser");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String username=resultSet.getString("username");

                Utilizator currentUser = new Utilizator(firstname,lastname,username);
                currentUser.setId(id);
                users.add(currentUser);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return new Page<>(users, numberOfElements);
    }
    public Optional<Utilizator> findOnesAccount(String username, String hashedPassword)
    {
        try(Connection connection = DriverManager.getConnection(dbConnection.DB_URL, dbConnection.DB_USER, dbConnection.DB_PASSWD);
            PreparedStatement statement = connection.prepareStatement("select * from Utilizatori " +
                    "where username = ? and password=?");

        ) {
            statement.setString(1,username);
            statement.setString(2,hashedPassword);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                Long longID=resultSet.getLong(1);
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                Utilizator u = new Utilizator(firstName,lastName,username);
                u.setId(longID);
                u = loadFriends(u).get();
                return Optional.ofNullable(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
