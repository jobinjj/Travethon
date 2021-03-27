package uk.ac.tees.w9501293.travethon.room.users;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UsersDao {
    @Query("SELECT * FROM users")
    List<Users> getAll();

    @Query("SELECT * FROM users WHERE uid IN (:usersIds)")
    List<Users> loadAllByIds(String[] usersIds);

    @Query("SELECT * FROM users WHERE uid  LIKE :userId LIMIT 1")
    Users loadById(String userId);

    @Query("SELECT * FROM users WHERE username LIKE :username LIMIT 1")
    Users findByName(String username);

    @Insert
    void insertAll(Users... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Users user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Users> user);

    @Delete
    void delete(Users users);
}
