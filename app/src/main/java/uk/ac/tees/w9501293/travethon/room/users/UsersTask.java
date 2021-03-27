package uk.ac.tees.w9501293.travethon.room.users;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import java.util.List;

import uk.ac.tees.w9501293.travethon.room.AppDatabase;

public class UsersTask {
    public AsyncTask<Void, Void, Void> addUsers(Context context, List<Users> users){
        return new AddUsers(context,users).execute();
    }

    public AsyncTask<Void, Void, Void> addUser(Context context, Users user){
        return new AddUser(context,user).execute();
    }
    public AsyncTask<Void, Void, Users> getUser(Context context, String uid,getUserListener getUserListener){
        return new GetUser(context,getUserListener,uid).execute();
    }
    public AsyncTask<Void, Void, List<Users>> getAllUsers(Context context, getUsersListener getUsersListener){
        return new GetUsers(context,getUsersListener).execute();
    }

    private class AddUsers extends AsyncTask<Void,Void,Void>{
        private Context context;
        private List<Users> users;

        public AddUsers(Context context, List<Users> users) {
            this.context = context;
            this.users = users;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                AppDatabase db = Room.databaseBuilder(context,
                        AppDatabase.class, "Travethon").build();
                UsersDao usersDao = db.usersDao();
                usersDao.insertAll(users);
            }catch (Exception e){
                Log.e("UsersTask", "adduser: " + e.getMessage() );
            }

            return null;
        }
    }
    private class AddUser extends AsyncTask<Void,Void,Void>{
        private Context context;
        private Users users;

        public AddUser(Context context, Users users) {
            this.context = context;
            this.users = users;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                AppDatabase db = Room.databaseBuilder(context,
                        AppDatabase.class, "Travethon").build();
                UsersDao usersDao = db.usersDao();
                usersDao.insert(users);
            }catch (Exception e){
                Log.e("UsersTask", "adduser: " + e.getMessage() );
            }

            return null;
        }
    }

    private class GetUser extends AsyncTask<Void,Void,Users>{
        private Context context;
        private getUserListener getUserListener;
        private String uid;

        public GetUser(Context context, getUserListener getUserListener,String uid) {
            this.context = context;
            this.getUserListener = getUserListener;
            this.uid = uid;
        }

        @Override
        protected Users doInBackground(Void... voids) {
            try{
                AppDatabase db = Room.databaseBuilder(context,
                        AppDatabase.class, "Travethon").build();
                UsersDao usersDao = db.usersDao();
                return usersDao.loadById(uid);
            }catch (Exception e){
                Log.e("MainActivity", "doInBackground: " + e.getMessage() );
            }

            return null;
        }

        @Override
        protected void onPostExecute(Users user) {
            super.onPostExecute(user);
            getUserListener.onFetched(user);
        }
    }

    private class GetUsers extends AsyncTask<Void,Void,List<Users>>{
        private Context context;
        private getUsersListener getUsersListener;

        public GetUsers(Context context, getUsersListener getUsersListener) {
            this.context = context;
            this.getUsersListener = getUsersListener;
        }

        @Override
        protected List<Users> doInBackground(Void... voids) {
            try{
                AppDatabase db = Room.databaseBuilder(context,
                        AppDatabase.class, "Travethon").build();
                UsersDao usersDao = db.usersDao();
                return usersDao.getAll();
            }catch (Exception e){
                Log.e("MainActivity", "doInBackground: " + e.getMessage() );
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Users> users) {
            super.onPostExecute(users);
            getUsersListener.onFetched(users);
        }
    }

    public interface getUserListener{
        void onFetched(Users user);
    }

    public interface getUsersListener{
        void onFetched(List<Users> users);
    }
}
