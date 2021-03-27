package uk.ac.tees.w9501293.travethon.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import uk.ac.tees.w9501293.travethon.room.users.Users;
import uk.ac.tees.w9501293.travethon.room.users.UsersDao;

@Database(entities = {Users.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UsersDao usersDao();
}
