package pt4.flotsblancs.database;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import pt4.flotsblancs.database.model.User;

public class UserStore {

    private UserStore(){}

    private User user;

    private static UserStore instance = null;

    public static UserStore getInstance(){
        if (instance == null) {
            instance = new UserStore();
        }
        return instance;
    }

    private User getUser(){
        return user;
    }

    public static User getUserInstance(){
        return getInstance().getUser();
    }

    public void setUser(User u){
        user = u;
    }
    
    public static boolean log(String id, String mdp){
        QueryBuilder<User, String> queryBuilder;
		try {
			queryBuilder = Database.getInstance().getUsersDao().queryBuilder();
			queryBuilder.where().eq("login", id);
			PreparedQuery<User> preparedQuery = queryBuilder.prepare();
			List<User> accountList = Database.getInstance().getUsersDao().query(preparedQuery);
	
			for (User u : accountList) {                
				if (u.getPassword().equals(mdp)){
                    getInstance().setUser(u);
                    return true;
                }
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
        return false;
    }
}
