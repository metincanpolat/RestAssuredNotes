package POJO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ToDo {

     int userId;
     int id;
     String title;
     boolean completed;

    @Override
    public String toString() {
        return "ToDo{" +
                "userId=" + userId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                '}';
    }
}
