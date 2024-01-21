package models;

import lombok.Data;

@Data
public class UserCreateResponseModel {
    String name, job, createdAt, id;
}
