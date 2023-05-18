package gr.aueb.cf.movieapp.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;
    private String username;
    private String password;
    private List<Object> favoriteList = new ArrayList<>();


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, List<Object> favoriteList) {
        this.username = username;
        this.favoriteList = favoriteList;
    }
    public void addFavorite(Object movieID) {
        if (this.favoriteList.contains((movieID))) {
            System.out.println("Already in favorites");
        } else {
            this.favoriteList.add(movieID);
        }
    }

    public void removeFavorite(String movieID) {
        this.favoriteList.remove(movieID);
    }
}
