package gr.aueb.cf.movieapp.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.management.InstanceAlreadyExistsException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Document("users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private BigInteger id;
    private String username;
    private String password;
    private List<String> favoriteList = new ArrayList<>();


    // Constructor ΧΩΡΙΣ id. Η MongoDB παρέχει για κάθε instance ένα δικό της, μοναδικό _id
    public User(String username, String password, List<String> favoriteList) {
        this.username = username;
        this.password = password;
        this.favoriteList = favoriteList;
    }

    public void addFavorite(String movieID) throws InstanceAlreadyExistsException {
        if (this.favoriteList.contains((movieID))) {
            throw new InstanceAlreadyExistsException();
        } else {
            this.favoriteList.add(movieID);
        }
    }

    public void removeFavorite(String movieID) {
        this.favoriteList.remove(movieID);
    }
}
