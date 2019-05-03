package models;

import org.apache.commons.lang.StringUtils;
import play.db.jpa.Model;

import javax.persistence.Entity;
import java.util.List;

/**
 * Created by ahmet.bayirli on 19.8.2015.
 */
@Entity
public class User extends Model
{

    public String userName;
    public String password;
    public Integer role;

    public User(String userName, String password, Integer role)
    {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }


    public boolean isEmpty()
    {
        return id==null&&StringUtils.isEmpty(userName)&&StringUtils.isEmpty(password)&& role ==null;
    }

    @Override
    public String toString()
    {
        return userName;
    }


    public static User findByKullaniciAdiSifre(User user) {
        return User.find("userName = ? and password = ?", user.userName, user.password).first();
    }

    public static User findSuperAdmin(User user) {
        return find("userName = ? and password = ? and role=0", user.userName, user.password).first();
    }

    public static List<User> getNonSuperList() {
        return find("role>0").fetch();
    }

    public static User findByUserNameAndPassword_NonSuper(User user) {
        return find("userName = ? and password = ? and role>0", user.userName, user.password).first();
    }
}
