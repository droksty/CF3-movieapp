package gr.aueb.cf.movieapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDTO {
    private String username;
    private List<Object> favoriteList;
}
